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
public class Owlbear extends Player {
 
	private static final long serialVersionUID = 1L;

	public Owlbear() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Owlbear");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Owlbear.webp");

	    str(20).dex(12).con(17).inte(3).wis(12).cha(7);

		// Saving Throws
		strSave(+5).dexSave(+1).conSave(+3).intSave(-4).wisSave(+1).chaSave(-2);
	
		 // Combat Stats
		ac(13).init(+1).hp(59).speed(40).cr(3);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesmonstrosity);


        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Rend(), Rend()));


        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));
	}


	private Attack Rend() {
	
        Attack a = new Attack("Rend"); 
        a.setAttackBonus(7) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(5) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

}

