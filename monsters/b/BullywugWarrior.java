/**
 * 
 */
package com.dndcombat.monsters.b;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class BullywugWarrior extends Player {
 
	private static final long serialVersionUID = 1L;

	public BullywugWarrior() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Bullywug Warrior");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Bullywug Warrior.webp");

	    str(12).dex(14).con(13).inte(7).wis(10).cha(7);

		// Saving Throws
		strSave(+1).dexSave(+2).conSave(+1).intSave(-2).wisSave(+0).chaSave(-2);
	
		 // Combat Stats
		ac(15).init(+2).hp(11).speed(30).cr(0.25);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesfey);
 
        addAction(new Action("Insectile Rapier", RefList.actiontypesmainaction)
                .addAttack(InsectileRapier()));
	}


	private Attack InsectileRapier() {
	
        Attack a = new Attack("Insectile Rapier"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(8) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypespiercing);
        a.addDamage().nbrDice(1).dieType(4).damageTypeRefId(RefList.damagetypespoison);
        
        return a;
    }

}

