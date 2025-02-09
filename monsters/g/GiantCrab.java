/**
 * 
 */
package com.dndcombat.monsters.g;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class GiantCrab extends Player {
 
	private static final long serialVersionUID = 1L;

	public GiantCrab() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Giant Crab");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Giant Crab.webp");

	    str(13).dex(13).con(11).inte(1).wis(9).cha(3);

		// Saving Throws
		strSave(+1).dexSave(+1).conSave(+0).intSave(-5).wisSave(-1).chaSave(-4);
	
		 // Combat Stats
		ac(15).init(+1).hp(13).speed(30).cr(1);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesbeast);


        addAction(new Action("Claw", RefList.actiontypesmainaction)
                .addAttack(Claw()));
	}


	private Attack Claw() {
	
        Attack a = new Attack("Claw"); 
        a.setAttackBonus(3) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .setGrappleOnHit(true)
         .setGrappleDc(11)
         .setGrappleMediumOrSmaller(true)
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }

}

