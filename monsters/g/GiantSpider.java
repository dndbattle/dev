/**
 * 
 */
package com.dndcombat.monsters.g;

import java.util.Arrays;
import java.util.List;

import com.dndcombat.actions.InvalidActionException;
import com.dndcombat.actions.MovementManager;
import com.dndcombat.fight.conditions.Restrained;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.custom.RechargeModel;
import com.dndcombat.savingthrows.SaveResult;
import com.dndcombat.spells.basic.ISpell;
import com.dndcombat.spells.basic.RangeInFeet;
import com.dndcombat.spells.basic.SpellSlot;
import com.dndcombat.util.Roll;

/**
 
 */
public class GiantSpider extends Player {
 
	private static final long serialVersionUID = 1L;

	public GiantSpider() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Giant Spider");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Giant Spider.webp");

	    str(14).dex(16).con(12).inte(2).wis(11).cha(4);

		// Saving Throws
		strSave(+2).dexSave(+3).conSave(+1).intSave(-4).wisSave(+0).chaSave(-3);
	
		 // Combat Stats
		ac(14).init(+3).hp(26).speed(30).cr(1);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesbeast);


        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));
        addAction(Web());
        setMoveCloserAfterRound(1);
	}


	private Attack Bite() {
	
        Attack a = new Attack("Bite"); 
        a.setAttackBonus(5) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(8) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        a.addDamage().nbrDice(2).dieType(6).damageTypeRefId(RefList.damagetypespoison);
        
        return a;
    }


	/**
	 * Life Drain
	 */
	private Action Web() {
		Action web = new Action("Web", RefList.actiontypesmainaction);
		 
		web.setSpell(new ISpell() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String spellName = "Web";
			private RangeInFeet rangeInFeet = new RangeInFeet(60);
			private RechargeModel recharge = new RechargeModel(5);

			@Override
			public Action createInitialAction() {
				Action action = new Action(spellName, RefList.actiontypesmainaction);
				action.getFirstAttack().setAttackName(action.getActionName());
				action.setSpell(this, new SpellSlot(0));
				SaveDC useActionToSave = new SaveDC();  
				useActionToSave.dc =  13;
				useActionToSave.saveVsRefId = RefList.savingthrowvsdexterity;
				useActionToSave.conditionRefId = RefList.conditionsrestrained;
				action.setUseActionToSave(useActionToSave);

				return action;
			}

			@Override
			public List<Long> getSpellClasses() {
				return Arrays.asList();
			}


			@Override
			public int getMovementRequiredFeet(Player me, Action action, Player target, GameState state) {
				return new MovementManager().getRangedAttackMovementRequired(me, rangeInFeet, action, target, state);
			}

		 	 
			@Override
			public int evaluateScore(Player me, Player target, long actionTypeRefId, GameState state) throws InvalidActionException {
				recharge.check(me, spellName, state);
				// dont really want to use more than once per battle
				if (target.isImmuneToCondition(Restrained.class)
						|| target.hasModifier(spellName)
						|| state.enemyNextToMe(me)
						|| me.getBattleCastTimes(spellName) > 0
						) {
					throw new InvalidActionException();
				} 
				if (Roll.d20() < target.getArmorClass()) {
					throw new InvalidActionException();
				}
				if (target.isWeaponFighter()) {
					return 20;
				}
				throw new InvalidActionException();
			}
			
			
			@Override
			public void execute(Action action, Player me, Player target, GameState state) {
				recharge.use(state);
				affect(me, target, state);
				me.castSpell(action);
			}
			
			public void affect(Player me, Player target, GameState state) {
				// TODO Auto-generated method stub
		 
				Action action = createInitialAction();

				SaveResult sr = target.makeSavingThrow(action.getUseActionToSave(), action, me, state);
				if (!sr.passed) {
					//TODO not a break out, attack or burn
					state.addActionLog(me.getName() + " restrains " + target.getName() + " with "+spellName+", " + sr.getRollDisplay(), state);
					DurationType duration = new DurationType(10);
					Restrained restrained = new Restrained(me, createInitialAction(), duration, state);
					restrained.setSourceAction(action);
					target.addModifier(restrained);
					state.flagThisFight();
					
				} else {
					state.addActionLog(me.getName() + " misses " + target.getName() + " with "+spellName+", " + sr.getRollDisplay());
				}
			}

			@Override
			public Action createExecutionAction(Player me) {
				// TODO Auto-generated method stub
				return null;
			}
		  

			 
		}, new SpellSlot(0));
		return web;
	}
    
}

