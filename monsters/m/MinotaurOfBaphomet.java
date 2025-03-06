/**
 * 
 */
package com.dndcombat.monsters.m;

import com.dndcombat.actions.InvalidActionException;
import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.map.DnDDistanceCalculator;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.actions.ChargeDamage;

/**
 
 */
public class MinotaurOfBaphomet extends Player {
 
	private static final long serialVersionUID = 1L;

	public MinotaurOfBaphomet() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Minotaur of Baphomet");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Minotaur.webp");

	    str(18).dex(11).con(16).inte(6).wis(16).cha(9);

		// Saving Throws
		strSave(+4).dexSave(+0).conSave(+3).intSave(-2).wisSave(+3).chaSave(-1);
	
		 // Combat Stats
		ac(14).init(+0).hp(85).speed(40).cr(3);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesmonstrosity);

        addAction(new Action("Abyssal Glaive", RefList.actiontypesmainaction)
                .addAttack(AbyssalGlaive()));

        addAction(new Action("Gore", RefList.actiontypesmainaction)
                .addAttack(Gore()));
        
        getCreature().addModifierType(new ChargeDamage(new DamageModel().nbrDice(3).dieType(6).damageTypeRefId(RefList.damagetypespiercing)
        		.autoApplyCondition(RefList.conditionsprone,new DurationType(10)), 10, "Gore"));
        
        getCreature().addModifierType(new DefaultModifierType("Score") {
 
			private static final long serialVersionUID = 1L;
        	@Override
        	public int evaluateScore(Player me, Player target, Action action, long actionTypeRefId, GameState state) throws InvalidActionException {
        		if (action.is("Gore")) {
        			if (me.getBattleCastTimes("Charge") == 0) //TODO technically it recharges
            		if (DnDDistanceCalculator.roughDistanceInSquares(me, target) >=2 || me.getStatus().getLastMovementMade() >= 10){
            			return 20;
            		}
        		}
        		return 0;
        	}
        });
	}


	private Attack AbyssalGlaive() {
	
        Attack a = new Attack("Abyssal Glaive"); 
        a.setAttackBonus(6) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(12) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        a.addDamage().nbrDice(3).dieType(6).damageTypeRefId(RefList.damagetypesnecrotic);
        
        return a;
    }

	private Attack Gore() {
	
        Attack a = new Attack("Gore"); 
        a.setAttackBonus(6) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(4) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }

}

