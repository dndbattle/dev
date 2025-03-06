/**
 * 
 */
package com.dndcombat.monsters.a;

import java.util.List;

import com.dndcombat.actions.InvalidActionException;
import com.dndcombat.fight.conditions.Incapacitated;
import com.dndcombat.fight.model.AttackResult;
import com.dndcombat.fight.model.DurationType;
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
import com.dndcombat.monster.traits.MagicResistance;
import com.dndcombat.savingthrows.SaveResult;
import com.dndcombat.spells.wizard.Counterspell;
import com.dndcombat.util.Converter;

/**
 
 */
public class Arcanaloth extends Player {
 
	private static final long serialVersionUID = 1L;

	public Arcanaloth() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Arcanaloth");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Arcanaloth.webp");

	    str(17).dex(12).con(14).inte(20).wis(16).cha(17);

		// Saving Throws
		strSave(+3).dexSave(+5).conSave(+6).intSave(+9).wisSave(+7).chaSave(+3);
	
		 // Combat Stats
		ac(18).init(+5).hp(175).speed(30).flySpeed(30).cr(12);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesfiend);

		damageResist(RefList.damagetypescold);
		damageResist(RefList.damagetypesfire);
		damageResist(RefList.damagetypeslightning);
		damageImmune(RefList.damagetypespoison);
		damageImmune(RefList.damagetypesacid);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionscharmed);
		getCreature().addModifierType(new MagicResistance());
		setMonsterSpellDC(17);

	/*
	- Multiattack for melee and ranged
	- addDamage on each attack
	
	- saving throws ending on the targets next turn have a duration of 0
	- saving throws triggered on hit can be added to the action (duration customized)
	- save or damage/effect is WithinXFeetEffect w = new WithinXFeetEffect(5, a.getAttackName(), damage, save, 0); see gladiator. 1/2 dmg on save is not the default
	
	- legendary action type and cost need to be set if you have legendary actions
	- legendary action Within spell needs to be set on the action for some reason
	*/
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( FiendishBurst(), FiendishBurst(), BanishingClaw()));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( FiendishBurstThrown(), FiendishBurstThrown(), FiendishBurstThrown()));


       addAction(new Action("Fiendish Burst", RefList.actiontypesmainaction)
                .addAttack(FiendishBurst()));


       addAction(new Action("Fiendish Burst Ranged", RefList.actiontypesmainaction)
                .addAttack(FiendishBurstThrown()));


        addAction(new Action("Banishing Claw", RefList.actiontypesmainaction)
                .addAttack(BanishingClaw()));
        
        getCreature().addModifierType(new Counterspell());
        
        addTome();
	}

    private Attack FiendishBurst() {
        Attack a = new Attack("Fiendish Burst"); 
        a.setAttackBonus(9) 
         .setMeleereach(5) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(4) 
         .dieType(12) 
         .bonus(5) 
         .damageTypeRefId(RefList.damagetypesnecrotic); 
        return a;
    }
    
    private Attack FiendishBurstThrown() {
        Attack a = new Attack("Fiendish Burst Thrown"); 
        a.setAttackBonus(9) 
         .setAttackRange(120) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(4) 
         .dieType(12) 
         .bonus(5) 
         .damageTypeRefId(RefList.damagetypesnecrotic); 
        return a;
    }

	private Attack BanishingClaw() {
	
        Attack a = new Attack("Banishing Claw"); 
        a.setAttackBonus(9) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(4) 
         .bonus(5) 
         .damageTypeRefId(RefList.damagetypesslashing);
        a.addDamage().nbrDice(3).dieType(12).damageTypeRefId(RefList.damagetypespsychic);
        
        return a;
    }
	
	private void addTome() {
		String name = "Banishing Claw";
		  getCreature().addModifierType(new DefaultModifierType(name) {
				private static final long serialVersionUID = 1L;
				
				@Override
				public int evaluateScore(Player me, Player target, Action action, long actionTypeRefId, GameState state) throws InvalidActionException {
					if (action.is("Multiattack") && action.hasAttack(name) && target.isNextTo(me)) {
						return 50;
					}
					return 0;
				}
				
				@Override
				public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target, List<Damage> newdmglist, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
					if (!attack.is(name)) {
						return false;
					}
					SaveDC save = new SaveDC().dc(17).saveVsRefId(RefList.savingthrowvscharisma);
					SaveResult sr = target.makeSavingThrow(save, action, source, state);
					state.addActionLog("ok " + System.nanoTime());
					if (sr.passed) {
						state.addActionLog(target.getName() + action.getActionName() + " " + attack.getAttackName() + " avoids being trapped inside the Soul Tome, " + sr.getRollDisplay());
					} else {
						bind(source, action, attack, target, state, sr);
					}
					return false;
				}
	        	
	        });
	        
	}
	
	private void bind(Player source, Action action, Attack attack, Player target, GameState state, SaveResult sr ) {
		state.addActionLog(target.getName() + action.getActionName() + " " + attack.getAttackName() + " is trapped inside the Soul Tome, " + sr.getRollDisplay());
		Modifier m = new Modifier("Trapped inside Soul Tome", new DefaultModifierType("Trapped inside Soul Tome") {

			private static final long serialVersionUID = 1L;
			public boolean applyModifierOnMeStartOfMyTurnRemoveIfTrue(Player me, Modifier m, GameState state) {
				int count = Converter.convertInteger(me.getStatus().map.getOrDefault("soulTomeEscapeTries", "0"));
				if (count > 2) { 
					return false;
				}
				SaveDC save = new SaveDC().dc(17).saveVsRefId(RefList.savingthrowvscharisma);
				SaveResult sr = target.makeSavingThrow(save, action, source, state);
				if (sr.passed) {
					state.addActionLog(me.getName() + " escapes the soul tome, " + sr.getRollDisplay());
					return true;
				} else {
					state.addActionLog(me.getName() + " fails to escape the soul tome, " + sr.getRollDisplay());
					me.getStatus().map.put("soulTomeEscapeTries", String.valueOf(count+1));
				}
				
				return false;
			}

		}
				, source, action, new DurationType(10), state);
		m.setIsConditions(Incapacitated.class);
		target.addModifier(state,m);

	}

}

