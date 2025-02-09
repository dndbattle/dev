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
public class Guard extends Player {
 
	private static final long serialVersionUID = 1L;

	public Guard() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Guard");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Guard.webp");

	    str(13).dex(12).con(12).inte(10).wis(11).cha(10);

		// Saving Throws
		strSave(+1).dexSave(+1).conSave(+1).intSave(+0).wisSave(+0).chaSave(+0);
	
		 // Combat Stats
		ac(16).init(+1).hp(11).speed(30).cr(0.125);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);


       addAction(new Action("Spear", RefList.actiontypesmainaction)
                .addAttack(Spear()));


       addAction(new Action("Spear Thrown", RefList.actiontypesmainaction)
                .addAttack(SpearThrown()));

	}

    private Attack Spear() {
        Attack a = new Attack("Spear"); 
        a.setAttackBonus(3) 
         .setMeleereach(5) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        return a;
    }
    private Attack SpearThrown() {
        Attack a = new Attack("Spear Thrown"); 
        a.setAttackBonus(3) 
         .setAttackRange(20) // 0 if not found
         .setLongRange(60) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        return a;
    }

}

