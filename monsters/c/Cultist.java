/**
 * 
 */
package com.dndcombat.monsters.c;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class Cultist extends Player {
 
	private static final long serialVersionUID = 1L;

	public Cultist() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Cultist");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Cultist.webp");

	    str(11).dex(12).con(10).inte(10).wis(11).cha(10);

		// Saving Throws
		strSave(+0).dexSave(+1).conSave(+0).intSave(+0).wisSave(+2).chaSave(+0);
	
		 // Combat Stats
		ac(12).init(+1).hp(9).speed(30).cr(1);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);

        addAction(new Action("Ritual Sickle", RefList.actiontypesmainaction)
                .addAttack(RitualSickle()));
	}


	private Attack RitualSickle() {
	
        Attack a = new Attack("Ritual Sickle"); 
        a.setAttackBonus(3) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(4) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypesslashing);
        a.addDamage().bonus(1).damageTypeRefId(RefList.damagetypesnecrotic);
        
        return a;
    }

}

