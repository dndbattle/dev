/**
 * 
 */
package com.dndcombat.monsters.n;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.Parry;

/**
 
 */
public class Noble extends Player {
 
	private static final long serialVersionUID = 1L;

	public Noble() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Noble");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Noble.webp");

	    str(11).dex(12).con(11).inte(12).wis(14).cha(16);

		// Saving Throws
		strSave(+0).dexSave(+1).conSave(+0).intSave(+1).wisSave(+2).chaSave(+3);
	
		 // Combat Stats
		ac(15).init(+1).hp(9).speed(30).cr(0.125);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);


        addAction(new Action("Rapier", RefList.actiontypesmainaction)
                .addAttack(Rapier()));
        
        getCreature().addModifierType(new Parry(2));
	}


	private Attack Rapier() {
	
        Attack a = new Attack("Rapier"); 
        a.setAttackBonus(3) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(8) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }

}

