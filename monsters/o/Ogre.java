/**
 * 
 */
package com.dndcombat.monsters.o;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class Ogre extends Player {
 
	private static final long serialVersionUID = 1L;

	public Ogre() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Ogre");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Ogre.webp");

	    str(19).dex(8).con(16).inte(5).wis(7).cha(7);

		// Saving Throws
		strSave(+4).dexSave(-1).conSave(+3).intSave(-3).wisSave(-2).chaSave(-2);
	
		 // Combat Stats
		ac(11).init(-1).hp(68).speed(40).cr(2);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesgiant);


        addAction(new Action("Greatclub", RefList.actiontypesmainaction)
                .addAttack(Greatclub()));
 
       addAction(new Action("Javelin Thrown", RefList.actiontypesmainaction)
                .addAttack(JavelinThrown()));

	}


	private Attack Greatclub() {
	
        Attack a = new Attack("Greatclub"); 
        a.setAttackBonus(6) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }
   
    private Attack JavelinThrown() {
        Attack a = new Attack("Javelin Thrown"); 
        a.setAttackBonus(6) 
         .setAttackRange(30) // 0 if not found
         .setLongRange(120) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        return a;
    }

}

