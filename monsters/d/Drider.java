/**
 * 
 */
package com.dndcombat.monsters.d;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class Drider extends Player {
 
	private static final long serialVersionUID = 1L;

	public Drider() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Drider");
//   
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Drider.webp");

	    str(16).dex(19).con(18).inte(13).wis(16).cha(12);

		// Saving Throws
		strSave(+3).dexSave(+4).conSave(+4).intSave(+1).wisSave(+3).chaSave(+1);
	
		 // Combat Stats
		ac(19).init(+4).hp(123).speed(30).cr(6);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesmonstrosity);

	 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Foreleg(),Foreleg(),Foreleg() ));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(PoisonBurst(),PoisonBurst(),PoisonBurst() ));


        addAction(new Action("Foreleg", RefList.actiontypesmainaction)
                .addAttack(Foreleg()));
        
        addAction(new Action("Poison Burst", RefList.actiontypesmainaction)
                .addAttack(PoisonBurst()));

	}


	private Attack Foreleg() {
	
        Attack a = new Attack("Foreleg"); 
        a.setAttackBonus(7) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }
    private Attack PoisonBurst() {
        Attack a = new Attack("Poison Burst"); 
        a.setAttackBonus(6) 
         .setAttackRange(120) 
         .setLongRange(0) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .dieType(6) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypespoison); 
         
        // a.setThrownRange(120, 0); 
        return a;
    }

}

