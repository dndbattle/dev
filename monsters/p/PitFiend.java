/**
 * 
 */
package com.dndcombat.monsters.p;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dndcombat.actions.ActionManager;
import com.dndcombat.actions.DamageManager;
import com.dndcombat.actions.InvalidActionException;
import com.dndcombat.actions.MainActionAgainException;
import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.conditions.Frightened;
import com.dndcombat.fight.conditions.Poisoned;
import com.dndcombat.fight.conditions.UnableToHeal;
import com.dndcombat.fight.map.DnDDistanceCalculator;
import com.dndcombat.fight.model.AttackResult;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.DurationType.WhenToEnd;
import com.dndcombat.fight.model.DurationType.WhoseTurn;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Modifier;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.Damage;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.SphereBreath;
import com.dndcombat.monster.traits.MagicResistance;
import com.dndcombat.savingthrows.SaveResult;
import com.dndcombat.spells.basic.ISpell;
import com.dndcombat.spells.basic.SpellSlot;
import com.dndcombat.spells.wizard.Fireball;
import com.dndcombat.util.Roll;
import com.dndcombat.util.RollResult;

/**
 
 */
public class PitFiend extends Player {
 
	private static final long serialVersionUID = 1L;

	public PitFiend() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Pit Fiend");
//   
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Pit Fiend.webp");

	    str(26).dex(14).con(24).inte(22).wis(18).cha(24);

		// Saving Throws
		strSave(+8).dexSave(+8).conSave(+7).intSave(+6).wisSave(+10).chaSave(+7);
	
		 // Combat Stats
		ac(21).init(+14).hp(337).speed(30).flySpeed(60).cr(20);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesfiend);

		getCreature().setLegendaryResistances(4);

		getCreature().setLegendaryActions(4);
		setMonsterSpellDC(21);

		damageResist(RefList.damagetypescold);
		damageImmune(RefList.damagetypespoison);
		damageImmune(RefList.damagetypesfire);
		conditionImmune(RefList.conditionspoisoned);
		getCreature().addModifierType(new MagicResistance());
 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Bite(), DevilishClaw(), DevilishClaw(), FieryMace() ));

        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));
        
        addAction(new Action("Devilish Claw", RefList.actiontypesmainaction)
                .addAttack(DevilishClaw()));

        addAction(new Action("Fiery Mace", RefList.actiontypesmainaction)
                .addAttack(FieryMace()));
        
        addAction(HellFire());
        getCreature().addModifierType(new DefaultModifierType("Hellfire") {
 
        	@Override
        	public int evaluateScore(Player me, Player target, Action action, long actionTypeRefId, GameState state) throws InvalidActionException {
        		// make sure you only evaluate the right action
        		if (action.is("Hellfire")) {
            		int amt = new Fireball().evaluateScore(me, action, target, actionTypeRefId, state);
            		return amt * 2; // double fireball
        		} else {
        			return 0;
        		}
        	}
        	
			private static final long serialVersionUID = 1L;
			@Override
			public void afterPerformSpellAction(Player me, ISpell spell, Action action, Player target, GameState state) throws MainActionAgainException {
				//TODO hold monster on someone
				if (action.is("Hellfire") && !me.hasCastThisTurn("Second Fireball")) {
					me.markCast("Second Fireball");
					new ActionManager().performAction(me, target, action, state);
				}
			}
        });
        
        getCreature().addModifierType(new DefaultModifierType("Fear Aura") {
 
			private static final long serialVersionUID = 1L;
        	@Override
        	public void applyModifierTypeOnSomeoneOnMyTurn(Player someone, Player me, GameState state) {
        		if (DnDDistanceCalculator.roughDistanceInSquares(me, someone) <= 4) {
        			if (me.isImmuneTo("Fear Aura") || me.isImmuneToCondition(Frightened.class)) {
        				return;
        			}
        			SaveDC save = new SaveDC().dc(21).saveVsRefId(RefList.savingthrowvswisdom).conditionRefId(RefList.conditionsfrightened);
        			Action action = new Action("Fear Aura", RefList.actiontypesfreeaction);
        			SaveResult sr = me.makeSavingThrow(save, action, someone, state);
        			if (sr.passed) {
        				me.markImmuneTo("Fear Aura");
        				state.addActionLog(me.getName() +  " resists the fear aura, " + sr.getRollDisplay());
        			} else if (!me.isFrightened()) {
        				me.addModifier(state, new Frightened(someone, action, new DurationType(1).whoseTurn(WhoseTurn.TargetTurn).whenToEnd(WhenToEnd.StartOfTurn), state));
        				state.addActionLog(me.getName() +  " is frightened, " + sr.getRollDisplay());
        			}
        		}
        	}
        });
	}
    

	private Action HellFire() {
		Action a = new Action("Hellfire", RefList.actiontypesmainaction);
		BreathWeapon bw =BreathWeapon.builder()
				.breathName(a.getActionName())
				.shape("Sphere")
				.coneOrSphereSizeFeet(20)
				.recharge(4)
				.saveDC(21)
				.saveVsRefId(RefList.savingthrowvsdexterity)
				.actionTypeRefId(RefList.actiontypesmainaction)
				.damage(Arrays.asList(new DamageModel().nbrDice(10).dieType(6).damageTypeRefId(RefList.damagetypesfire)))
				.build();
		a.setSpell(new SphereBreath(bw), new SpellSlot(0));
		
		return a;
	}

	private Attack Bite() {
		 
        Attack a = new Attack("Bite"); 
        a.setAttackBonus(14) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .dieType(6) 
         .bonus(8) 
         .damageTypeRefId(RefList.damagetypespiercing); 
         a.addModifierType(new DefaultModifierType("Poisoned")  {
 
			private static final long serialVersionUID = 1L;
        	@Override
        	public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target, List<Damage> newdmglist, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
        		if (target.isUnableToHeal()) {
        			return false; // already poisoned
        		}
        		SaveDC save = new SaveDC();
        		save.saveVsRefId(RefList.savingthrowvsconstitution).dc(21).conditionRefId(RefList.conditionspoisoned);
        		action.setEndOfTurnSave(save);
        		action.setActionName("Poisoned Bite"); // since it uses it in the log
        		
        		Modifier m = new Modifier("Poisoned", new DefaultModifierType("Poisoned") {
 
					private static final long serialVersionUID = 1L;
					
					@Override
					public boolean applyModifierOnMeStartOfMyTurnRemoveIfTrue(Player me, Modifier m, GameState state) {
						RollResult rr = Roll.nd(6, 6);
						List<Damage> damage = new ArrayList<Damage>();
						damage.add(new Damage(rr.getTotal(), RefList.damagetypespoison));
						state.addActionLog(me.getName() + " takes " + rr.getTotal() + " poison damage from bite");
						new DamageManager().applyDamage(source, action, action.getFirstAttack(), me, damage, state);
						return false;
					}
        			
        		}, source, action, new DurationType(10), state);
        		m.setIsConditions(Poisoned.class);
        		m.setIsConditions(UnableToHeal.class);
        		
        		target.addModifier(state, m);
        		
        		return false;
        	}
        });
        
        return a;
    }

	private Attack DevilishClaw() {
	
        Attack a = new Attack("Devilish Claw"); 
        a.setAttackBonus(14) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(4) 
         .dieType(8) 
         .bonus(8) 
         .damageTypeRefId(RefList.damagetypesnecrotic); 
        
        return a;
    }

	private Attack FieryMace() {
	
        Attack a = new Attack("Fiery Mace"); 
        a.setAttackBonus(14) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(4) 
         .dieType(6) 
         .bonus(8) 
         .damageTypeRefId(RefList.damagetypesforce);
        a.addDamage().nbrDice(6).dieType(6).damageTypeRefId(RefList.damagetypesfire);
        
        return a;
    }

}

