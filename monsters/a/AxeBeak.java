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
public class AxeBeak extends Player {
 
	private static final long serialVersionUID = 1L;

	public AxeBeak() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Axe Beak");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Axe Beak.webp");

	    str(14).dex(12).con(12).inte(2).wis(10).cha(5);

		// Saving Throws
		strSave(+2).dexSave(+1).conSave(+1).intSave(-4).wisSave(+0).chaSave(-3);
	
		 // Combat Stats
		ac(11).init(+1).hp(19).speed(50).cr(0.25);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesmonstrosity);
 
        addAction(new Action("Beak", RefList.actiontypesmainaction)
                .addAttack(Beak()));
	}


	private Attack Beak() {
	
        Attack a = new Attack("Beak"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(8) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

}

