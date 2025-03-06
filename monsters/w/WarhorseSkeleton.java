/**
 * 
 */
package com.dndcombat.monsters.w;

import java.util.List;

import com.dndcombat.fight.conditions.Prone;
import com.dndcombat.fight.model.AttackResult;
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
public class WarhorseSkeleton extends Player {
 
	private static final long serialVersionUID = 1L;

	public WarhorseSkeleton() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Warhorse Skeleton");
//   
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Warhorse Skeleton.webp");

	    str(18).dex(12).con(15).inte(2).wis(8).cha(5);

		// Saving Throws
		strSave(+4).dexSave(+1).conSave(+2).intSave(-4).wisSave(-1).chaSave(-3);
	
		 // Combat Stats
		ac(13).init(+1).hp(22).speed(60).cr(0.5);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesundead);

		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionspoisoned);
 
        addAction(new Action("Hooves", RefList.actiontypesmainaction)
                .addAttack(Hooves()));
        
        getCreature().addModifierType(new DefaultModifierType("Charge") {
        	 
			private static final long serialVersionUID = 1L;
         
        	@Override
        	public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target, List<Damage> newdmglist, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
        	 
    			if (source.getStatus().getLastMovementMade() >= 20) {
    				if (!target.isProne()) {
    					target.addModifier(state, new Prone(source, action, new DurationType(10), state));
    					state.addActionLog(source.getName() + " knocks " + target.getName() + " prone", state);
    					 
    				}
    			}
        		 
         		
        		return false;
        	}
        });
        
	}


	private Attack Hooves() {
	
        Attack a = new Attack("Hooves"); 
        a.setAttackBonus(6) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }

}

