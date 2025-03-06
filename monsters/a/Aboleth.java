/**
 * 
 */
package com.dndcombat.monsters.a;

import java.util.Arrays;
import java.util.List;

import com.dndcombat.actions.InvalidActionException;
import com.dndcombat.actions.SpellManager;
import com.dndcombat.actions.StrategyManager;
import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.Damage;
import com.dndcombat.model.DamageType;
import com.dndcombat.model.DieType;
import com.dndcombat.model.NbrOfDice;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.actions.Dominate;
import com.dndcombat.spells.basic.ISpell;
import com.dndcombat.spells.basic.RangeInFeet;
import com.dndcombat.util.Roll;

/**
 * 
 */

public class Aboleth extends Player {

 
	private static final long serialVersionUID = 1L;

	public Aboleth() {
		super(new Creature());
		create();
	}
	
	public void create() {
		
		getCreature().setCreatureName("Aboleth");
		setPlayersSide(false);
		getCreature().setImageUrl("/assets/monsters/MM/Aboleth.webp");
		str(21).dex(9).con(15).inte(18).wis(15).cha(18);
		strSave(5).dexSave(3).conSave(6).intSave(8).wisSave(6).chaSave(4);
		ac(17).init(7).hp(150).speed(10).cr(10);
		size(RefList.creaturesizeslarge).type(RefList.creaturetypesaberration);
		legendaryResistances(3).legendaryActions(3); //TODO 4 in lair
		
		// multi attack 
		addAction(new Action("Multiattack", RefList.actiontypesmainaction)
				.addAttack(tentacle(), tentacle(), consumeMemories()));
		addAction(new Action("Multiattack", RefList.actiontypesmainaction)
				.addAttack(tentacle(), tentacle(), dominateMind()));
		// legendary action 
		addAction(new Action("Lash", RefList.actiontypeslegendaryaction).addAttack(tentacle()).setLegendaryCost(1));
		addAction(new Action("Psychic Drain", RefList.actiontypeslegendaryaction).addAttack(consumeMemories()).setLegendaryCost(1));
	}

	private Attack tentacle() {
		Attack a = new Attack("Tentacle");
		a.setAttackBonus(9).setMeleereach(15).setRollToHitInd(1)
		.setGrappleOnHit(true)
		.setGrappleDc(14).
		setGrappleLargeOrSmaller(true)
		.setMaxNbrToGrapple(4)
		.getFirstDamage().nbrDice(2).dieType(6).bonus(5).damageTypeRefId(RefList.damagetypesbludgeoning);
		return a;
	}

	private Attack consumeMemories() {
		Attack a = new Attack("Consume Memories");
		a.setSpell(new ISpell() {
		 
			private static final long serialVersionUID = 1L;

			@Override
			public List<Long> getSpellClasses() {
				return null;
			}
			
			@Override
			public Action createInitialAction() {
				return new Action("Consume Memories", RefList.actiontypesmainaction);
			}
			
			@Override
			public Action createExecutionAction(Action a, Player me) {
				DamageModel damage = new DamageModel(new NbrOfDice(3), new DieType(6), new DamageType(RefList.damagetypespsychic));
				Action action = Action.createRangedAction(a, new RangeInFeet(30), damage);
				action.oneTimeSave.dc = 16;
				action.oneTimeSave.saveVsRefId = RefList.savingthrowvsintelligence;

				return action;
			}
			
			@Override
			public int getMovementRequiredFeet(Player me, Action action, Player target, GameState state) {
				return 0;
			}
			
			@Override
			public int evaluateScore(Player me, Action a, Player target, long actionTypeRefId, GameState state) throws InvalidActionException {
				if (!target.isCharmed() && !target.isGrappled()) {
					throw new InvalidActionException();
				}
				Action action = createExecutionAction(a, me);
				
				int amount = new StrategyManager().getAverageDamage(action, me, target, state);
				
				return amount;

			}
			
			@Override
			public void execute(Action a, Player me, Player target, GameState state) {
				if (!target.isCharmed() && !target.isGrappled()) {
					return; // not possible during multi attack
				}
				Action action = createExecutionAction(a, me);
				
				new SpellManager().castOneTimeSaveForHalf(me, action, target, state);
				me.heal(Arrays.asList(new Damage(Roll.d(10), RefList.damagetypesheal)), state);
				
			}
		
		
		});
		return a;
	}

	private Attack dominateMind() {
		Attack enslave = new Attack("Dominate Mind");
		enslave.setSpell(new Dominate(enslave.getAttackName(), 16, false));
		return enslave;
	}
	 
}
