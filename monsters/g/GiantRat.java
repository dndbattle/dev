/**
 * 
 */
package com.dndcombat.monsters.g;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.PackTactics;

/**
 
 */
public class GiantRat extends Player {
 
	private static final long serialVersionUID = 1L;

	public GiantRat() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Giant Rat");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Giant Rat.webp");

	    str(7).dex(16).con(11).inte(2).wis(10).cha(4);

		// Saving Throws
		strSave(-2).dexSave(+5).conSave(+0).intSave(-4).wisSave(+0).chaSave(-3);
	
		 // Combat Stats
		ac(13).init(+3).hp(7).speed(30).cr(1);

        // Size and Type
	    size(RefList.creaturesizessmall).type(RefList.creaturetypesbeast);

	    getCreature().addModifierType(new PackTactics());
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
        
        return a;
    }


}

