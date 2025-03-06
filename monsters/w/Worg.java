/**
 * 
 */
package com.dndcombat.monsters.w;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class Worg extends Player {
 
	private static final long serialVersionUID = 1L;

	public Worg() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Worg");
//   
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Worg.webp");

	    str(16).dex(13).con(13).inte(7).wis(11).cha(8);

		// Saving Throws
		strSave(+3).dexSave(+1).conSave(+1).intSave(-2).wisSave(+0).chaSave(-1);
	
		 // Combat Stats
		ac(13).init(+1).hp(26).speed(50).cr(0.5);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesfey);
 
        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));
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
        
        return a;
    }

}

