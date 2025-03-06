/**
 * 
 */
package com.dndcombat.monsters.w;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.PackTactics;

/**
 
 */
public class Wolf extends Player {
 
	private static final long serialVersionUID = 1L;

	public Wolf() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Wolf");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Wolf.webp");

	    str(14).dex(15).con(12).inte(3).wis(12).cha(6);

		// Saving Throws
		strSave(+2).dexSave(+2).conSave(+1).intSave(-4).wisSave(+1).chaSave(-2);
	
		 // Combat Stats
		ac(12).init(+2).hp(11).speed(40).cr(0.25);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesbeast);

	    getCreature().addModifierType(new PackTactics());

        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));
	}


	private Attack Bite() {
	
        Attack a = new Attack("Bite"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }

}

