/**
 * 
 */
package com.dndcombat.monsters.g;

import java.util.ArrayList;
import java.util.List;

import com.dndcombat.actions.DamageManager;
import com.dndcombat.actions.InvalidActionException;
import com.dndcombat.actions.MovementManager;
import com.dndcombat.fight.conditions.Restrained;
import com.dndcombat.fight.map.Node;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Location;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.messaging.ClientBrowser;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.Damage;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.savingthrows.SaveResult;
import com.dndcombat.spells.basic.ISpell;
import com.dndcombat.spells.basic.SpellSlot;
import com.dndcombat.util.Roll;
import com.dndcombat.util.RollResult;

/**
 
 */
public class GelatinousCube extends Player {
 
	private static final long serialVersionUID = 1L;

	public GelatinousCube() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Gelatinous Cube");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Gelatinous Cube.webp");

	    str(14).dex(3).con(20).inte(1).wis(6).cha(1);

		// Saving Throws
		strSave(+2).dexSave(-4).conSave(+5).intSave(-5).wisSave(-2).chaSave(-5);
	
		 // Combat Stats
		ac(6).init(-4).hp(63).speed(15).cr(2);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesooze);

		damageImmune(RefList.damagetypesacid);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionsfrightened);
		conditionImmune(RefList.conditionscharmed);
        addAction(new Action("Pseudopod", RefList.actiontypesmainaction)
                .addAttack(Pseudopod()));
        addAction(Engulf());
        getCreature().addModifierType(Engulfed());
	}


	private Attack Pseudopod() {
	
        Attack a = new Attack("Pseudopod"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .dieType(6) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypesacid); 
        
        return a;
    }
	

	/**
	 */
	private Action Engulf() {
		Action engulf = new Action("Engulf", RefList.actiontypesmainaction);
		engulf.setSpell(new ISpell() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<Long> getSpellClasses() {
				return new ArrayList<Long>();
			}
			
			@Override
			public int getMovementRequiredFeet(Player me, Action action, Player target, GameState state) {
				return new MovementManager().getMeleeAttackMovementRequired(me, action, target, state);
			}
			
			@Override
			public int evaluateScore(Player me, Action a, Player target, long actionTypeRefId, GameState state) throws InvalidActionException {
				if (target.getSizeDimension() > 2) {
					throw new InvalidActionException();
				}
				if (state.overlappingSomeone(me)) {
					throw new InvalidActionException();
				}
				return 40;
			}
			
			@Override
			public void execute(Action a, Player me, Player target, GameState state) {
				 state.addActionLog(me.getName() + " tries to engulf " + target.getName());
				 me.setLocation(target.getLocation());
				 ClientBrowser.updateLocation(me, state);
				 SaveDC save = new SaveDC();
				 save.dc = 12;
				 save.saveVsRefId = RefList.savingthrowvsdexterity;
				 tryToEngulf(me, target, save, a, state);
				 for (Player p : state.getEnemiesAlive(me)) {
					 if (p.isSame(target)) {
						 continue;
					 }
					 if (state.overlapping(me, p)) {
						 tryToEngulf(me, p, save, a, state);
					 }
				 }
			}
		
			@Override
			public Action createInitialAction() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Action createExecutionAction(Action a, Player me) {
				// TODO Auto-generated method stub
				return null;
			}
		}, new SpellSlot(0));
		return engulf;
	}
	
	private void tryToEngulf(Player me, Player target, SaveDC save, Action a, GameState state) {
		 SaveResult sr = target.makeSavingThrow(save, a, me, state);
		 if (sr.passed) {
			 Node newloc = state.getFreeLocationNextTo(target);
			 state.addActionLog(target.getName() + " dodges engulf and is pushed to " + newloc + ", " + sr.getRollDisplay());
			 target.setLocation(new Location(newloc.x, newloc.y));
			 ClientBrowser.updateLocation(target, state);
		 } else {
			 //TODO spellcasters need to move so not in imminent danger and should inflict death strikes if still on top of someone
			 state.addActionLog(me.getName() + " engulfs " + target.getName() + ", " + sr.getRollDisplay());
			 Action acid = new Action("Engulf",RefList.actiontypesfreeaction);
			 acid.getFirstAttack();
			 List<Damage> damage = new ArrayList<Damage>();
			 RollResult rr = Roll.nd(3, 6);
			 Damage d = new Damage();
			 d.diceDisplay = rr.getDisplay();
			 d.dmg = rr.getTotal();
			 d.damageTypeRefId = RefList.damagetypesacid;
			 damage.add(d);
			 new DamageManager().applyDamage(me, acid, acid.getFirstAttack(), target, damage, state);
			 
			 acid.getFirstAttack().restraintDc = 12;
			 Restrained restrained = new Restrained(me, acid, new DurationType(10), state);
			 target.addModifier(state,restrained);
		 }
	}
	
	private ModifierType Engulfed() {
		DefaultModifierType mt = new DefaultModifierType("Engulf") {
 			private static final long serialVersionUID = 1L;
 			
 			@Override
 			public void applyModifierTypeOnMeStartOfMyTurn(Player me, GameState state) {
 				for (Player target : state.getEnemiesAlive(me)) {
 					if (state.overlapping(me, target)) {
 						 Action acid = new Action("Engulf",RefList.actiontypesfreeaction);
 						 acid.getFirstAttack();
 						 List<Damage> damage = new ArrayList<Damage>();
 						 RollResult rr = Roll.nd(6, 6);
 						 Damage d = new Damage();
 						 d.diceDisplay = rr.getDisplay();
 						 d.dmg = rr.getTotal();
 						 d.damageTypeRefId = RefList.damagetypesacid;
 						 damage.add(d);
 						 state.addActionLog(me.getName() + " has " + target.getName() + " engulfed");
 						 new DamageManager().applyDamage(me, acid, acid.getFirstAttack(), target, damage, state);
 						 me.getStatus().setMovementLeft(0);// not moving
 					}
 				}
 			 
 			}
			
		};
		return mt;
	}
}

