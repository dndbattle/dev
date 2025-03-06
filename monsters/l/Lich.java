/**
 * 
 */
package com.dndcombat.monsters.l;

import java.util.Arrays;
import com.dndcombat.actions.MainActionAgainException;
import com.dndcombat.actions.MovementManager;
import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.map.Node;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Location;
import com.dndcombat.fight.model.DurationType.WhenToEnd;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.messaging.ClientBrowser;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.WithinXFeetEffect;
import com.dndcombat.monster.traits.Parry;
import com.dndcombat.spells.bard.ChainLightning;
import com.dndcombat.spells.bard.Fear;
import com.dndcombat.spells.basic.ISpell;
import com.dndcombat.spells.basic.SpellSlot;
import com.dndcombat.spells.warlock.FingerOfDeath;
import com.dndcombat.spells.wizard.PowerWordKill;
import com.dndcombat.util.Converter;

/**
 
 */
public class Lich extends Player {
 
	private static final long serialVersionUID = 1L;

	public Lich() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Lich");
//   
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Lich.webp");

	    str(11).dex(16).con(16).inte(21).wis(14).cha(16);

		// Saving Throws
		strSave(+0).dexSave(+10).conSave(+10).intSave(+12).wisSave(+9).chaSave(+3);
	
		 // Combat Stats
		ac(20).init(+17).hp(315).speed(30).cr(21);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesundead);

		getCreature().setLegendaryResistances(4);
		getCreature().setLegendaryActions(4);
		
		setMonsterSpellDC(20);
		
		damageResist(RefList.damagetypescold);
		damageResist(RefList.damagetypeslightning);
		damageImmune(RefList.damagetypesnecrotic);
		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionsfrightened);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionscharmed);
		conditionImmune(RefList.conditionsparalyzed);
	 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(EldritchBurst(),EldritchBurst(), ParalyzingTouch() ));
 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(EldritchBurstThrown(),EldritchBurstThrown(),EldritchBurstThrown()));
 
       addAction(new Action("Eldritch Burst", RefList.actiontypesmainaction)
                .addAttack(EldritchBurst()));

        addAction(new Action("Eldritch Burst Thrown", RefList.actiontypesmainaction)
                .addAttack(EldritchBurstThrown()));

        addAction(new Action("Paralyzing Touch", RefList.actiontypesmainaction)
                .addAttack(ParalyzingTouch()));
        
        getCreature().addModifierType(new Parry("Shield", 5));
        
        addAction(new Action("Chain Lightning", RefList.actiontypesmainaction).setLimitedUses(1).setMonsterSpell(true).setSpell(new ChainLightning(), new SpellSlot(0)));
        
        addAction(new Action("Power Word Kill", RefList.actiontypesmainaction).setLimitedUses(1).setMonsterSpell(true).setSpell(new PowerWordKill(), new SpellSlot(0)));
        
        addAction(new Action("Finger of Death", RefList.actiontypesmainaction).setLimitedUses(1).setMonsterSpell(true).setSpell(new FingerOfDeath(), new SpellSlot(0)));
        
        addAction(disuptLife());
        
        addAction(new Action("Fear", RefList.actiontypeslegendaryaction).setMaxUsesPerRound(1).setMonsterSpell(true).setMonsterSpell(new Fear()));
        
        addAction(deathlyTeleport());
        
        setMoveCloserAfterRound(4);
	}

    private Action deathlyTeleport() {
		Action action = new Action("Deathly Teleport", RefList.actiontypeslegendaryaction);
		WithinXFeetEffect w = new WithinXFeetEffect(10, action.getActionName(), Arrays.asList(new DamageModel().nbrDice(2).dieType(10).damageTypeRefId(RefList.damagetypesnecrotic))
				, null, 1);
		action.setSpell(w, new SpellSlot(0));
		action.setMonsterSpell(true);
		
		getCreature().addModifierType(new DefaultModifierType(action.getActionName()) {
		 
			private static final long serialVersionUID = 1L;

			@Override
			public void afterPerformSpellAction(Player me, ISpell spell, Action action, Player target, GameState state) throws MainActionAgainException {
				
			    if (me.hasLegendaryAction() && Converter.isTrimmedSameIgnoreCase(action.getActionName(), "Deathly Teleport")) {
			     
			    	int teleportRangeInFeet = 60;
			    	int maxAttackRangeInFeet = 60;
			    	Node bestSquare = new MovementManager().moveAwayFromEnemies(me, teleportRangeInFeet, maxAttackRangeInFeet, state);

			        // If a suitable square was found, perform the teleport
			        if (bestSquare != null) {
			            // Assuming you have a method to move the player to the new position
			            me.setLocation(new Location(bestSquare.x, bestSquare.y));
			            state.addActionLog(getName() + " teleports to (" + bestSquare.x + ", " + bestSquare.y + ")");
			            me.useLegendaryAction();
						ClientBrowser.updateLocation(me, state);
			        }
			    }
			}
		});
		return action;
	}

    private Action disuptLife() {
		Action action = new Action("Disrupt Life", RefList.actiontypeslegendaryaction);
		SaveDC save = new SaveDC().dc(20).saveVsRefId(RefList.savingthrowvsconstitution);
		WithinXFeetEffect w = new WithinXFeetEffect(20, action.getActionName(), Arrays.asList(new DamageModel().nbrDice(9).dieType(6).damageTypeRefId(RefList.damagetypesnecrotic))
				, save, 1);
		w.attackSettings.halfDamage = true; // half on save
		action.setMaxUsesPerRound(1);
		action.setSpell(w, new SpellSlot(0));
		action.setMonsterSpell(true);
		return action;
	}

	private Attack EldritchBurst() {
        Attack a = new Attack("Eldritch Burst"); 
        a.setAttackBonus(12) 
         .setMeleereach(5) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(4) 
         .dieType(12) 
         .bonus(5) 
         .damageTypeRefId(RefList.damagetypesforce); 
        return a;
    }
    private Attack EldritchBurstThrown() {
        Attack a = new Attack("Eldritch Burst Thrown"); 
        a.setAttackBonus(12) 
         .setAttackRange(120) // 0 if not found
         .setLongRange(0) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(4) 
         .dieType(12) 
         .bonus(5) 
         .damageTypeRefId(RefList.damagetypesforce); 
        return a;
    }

	private Attack ParalyzingTouch() {
	
        Attack a = new Attack("Paralyzing Touch"); 
        a.setAttackBonus(12) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .autoApplyCondition(RefList.conditionsparalyzed, new DurationType(1).whenToEnd(WhenToEnd.StartOfTurn))
         .dieType(6) 
         .bonus(5) 
         .damageTypeRefId(RefList.damagetypescold); 
        
        return a;
    }

}

