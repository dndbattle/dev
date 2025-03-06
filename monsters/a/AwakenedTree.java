/**
 * 
 */
package com.dndcombat.monsters.a;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class AwakenedTree extends Player {
 
	private static final long serialVersionUID = 1L;

	public AwakenedTree() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Awakened Tree");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Awakened Tree.webp");

	    str(19).dex(6).con(15).inte(10).wis(10).cha(7);

		// Saving Throws
		strSave(+4).dexSave(-2).conSave(+2).intSave(+0).wisSave(+0).chaSave(-2);
	
		 // Combat Stats
		ac(13).init(-2).hp(59).speed(20).cr(2);

        // Size and Type
	    size(RefList.creaturesizeshuge).type(RefList.creaturetypesplant);

		damageResist(RefList.damagetypespiercing);
		damageResist(RefList.damagetypesbludgeoning);
	 
        addAction(new Action("Slam", RefList.actiontypesmainaction)
                .addAttack(Slam()));
	}


	private Attack Slam() {
	
        Attack a = new Attack("Slam"); 
        a.setAttackBonus(6) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }

}

