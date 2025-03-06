/**
 * 
 */
package com.dndcombat.monsters.g;

import com.dndcombat.actions.InvalidActionException;
import com.dndcombat.fight.conditions.Paralyzed;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.DurationType.WhenToEnd;
import com.dndcombat.fight.model.DurationType.WhoseTurn;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.util.Roll;

/**
 
 */
public class Ghoul extends Player {
 
	private static final long serialVersionUID = 1L;

	public Ghoul() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Ghoul");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Ghoul.webp");

	    str(13).dex(15).con(10).inte(7).wis(10).cha(6);

		// Saving Throws
		strSave(+1).dexSave(+2).conSave(+0).intSave(-2).wisSave(+0).chaSave(-2);
	
		 // Combat Stats
		ac(12).init(+2).hp(22).speed(30).cr(1);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesundead);

		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionscharmed);

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Bite(), Bite()));


        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));

        addAction(Claw());
        
        getCreature().addModifierType(new DefaultModifierType("Score") {
         
			private static final long serialVersionUID = 1L;

			@Override
        	public int evaluateScore(Player me, Player target, Action action, long actionTypeRefId, GameState state) throws InvalidActionException {
				if (action.is("Claw")) {
					if (target.getCreature().getCreatureTypeRefId()  == RefList.creaturetypesundead
							 || target.isElf()
							 || target.isParalyzed()
							 || target.isImmuneToCondition(Paralyzed.class)) {
						throw new InvalidActionException();
					} else {
						if (Roll.d(6) < 4) {
							return 30;
						}
					}
				}
				return 0;
        	}
        });
	}


	private Attack Bite() {
	
        Attack a = new Attack("Bite"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }

	private Action Claw() {
		Action action = new Action("Claw", RefList.actiontypesmainaction);
        
        Attack a = new Attack("Claw"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(4) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        action.addAttack(a);
        SaveDC save = new SaveDC();
        save.conditionRefId = RefList.conditionsparalyzed;
        save.dc = 10;
        save.saveVsRefId = RefList.savingthrowvsconstitution;
        save.duration = new DurationType(0);
        save.duration.whoseTurn = WhoseTurn.TargetTurn;
        save.duration.whenToEnd = WhenToEnd.EndOfTurn;
        
        action.oneTimeSave = save;
        
        return action;
    }

}

