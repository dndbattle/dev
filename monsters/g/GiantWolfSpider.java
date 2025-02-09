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
public class GiantWolfSpider extends Player {
 
	private static final long serialVersionUID = 1L;

	public GiantWolfSpider() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Giant Wolf Spider");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Giant Wolf Spider.webp");

	    str(12).dex(16).con(13).inte(3).wis(12).cha(4);

		// Saving Throws
		strSave(+1).dexSave(+3).conSave(+1).intSave(-4).wisSave(+1).chaSave(-3);
	
		 // Combat Stats
		ac(13).init(+3).hp(11).speed(40).cr(0.25);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesbeast);


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
         .dieType(4) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        a.addDamage().nbrDice(2).dieType(4).damageTypeRefId(RefList.damagetypespoison);
        
        return a;
    }

}

