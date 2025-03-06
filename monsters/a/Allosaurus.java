/**
 * 
 */
package com.dndcombat.monsters.a;

import java.util.List;

import com.dndcombat.actions.ActionManager;
import com.dndcombat.actions.InvalidActionException;
import com.dndcombat.actions.OpportunityAttackManager;
import com.dndcombat.fight.conditions.Prone;
import com.dndcombat.fight.map.DnDDistanceCalculator;
import com.dndcombat.fight.model.AttackResult;
import com.dndcombat.fight.model.AttackSettings;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.Damage;
import com.dndcombat.model.RefList;

/**
 
 */
public class Allosaurus extends Player {
 
	private static final long serialVersionUID = 1L;

	public Allosaurus() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Allosaurus");
//  
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Allosaurus.webp");

	    str(19).dex(13).con(17).inte(2).wis(12).cha(5);

		// Saving Throws
		strSave(+4).dexSave(+1).conSave(+3).intSave(-4).wisSave(+1).chaSave(-3);
	
		 // Combat Stats
		ac(13).init(+1).hp(51).speed(60).cr(2);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesbeast);

	  
        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));

        addAction(new Action("Claws", RefList.actiontypesmainaction)
                .addAttack(Claws()));
        
        
        getCreature().addModifierType(new DefaultModifierType("Charge") {
 
			private static final long serialVersionUID = 1L;
        	@Override
        	public int evaluateScore(Player me, Player target, Action action, long actionTypeRefId, GameState state) throws InvalidActionException {
        		if (action.is("Claws")) {
            		if (DnDDistanceCalculator.roughDistanceInSquares(me, target) >=6 || me.getStatus().getLastMovementMade() >= 30){
            			return 30;
            		}
        		}
        		return 0;
        	}
        	
        	@Override
        	public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target, List<Damage> newdmglist, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
        		if (action.is("Claws")) {
        			if (source.getStatus().getLastMovementMade() >= 30) {
        				if (!target.isProne()) {
        					target.addModifier(state, new Prone(source, action, new DurationType(10), state));
        					Action bite = new OpportunityAttackManager().getBestMeleeAction(target);
        					if (bite != null) {
        						state.addActionLog(source.getName() + " knocks " + target.getName() + " prone and uses bite", state);
        						new ActionManager().rollToHit(source, bite, bite.getFirstAttack(), target, state, new AttackSettings());
        					}
        				}
        			}
        		}
         		
        		return false;
        	}
        });
        
	}


	private Attack Bite() {
	
        Attack a = new Attack("Bite"); 
        a.setAttackBonus(6) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(10) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }

	private Attack Claws() {
	
        Attack a = new Attack("Claws"); 
        a.setAttackBonus(6) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(8) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

}

