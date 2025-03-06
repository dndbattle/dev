/**
 * 
 */
package com.dndcombat.monsters.s;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.Regeneration;

/**
 
 */
public class ShieldGuardian extends Player {
 
	private static final long serialVersionUID = 1L;

	public ShieldGuardian() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Shield Guardian");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Shield Guardian.webp");

	    str(18).dex(8).con(18).inte(7).wis(10).cha(3);

		// Saving Throws
		strSave(+4).dexSave(-1).conSave(+4).intSave(-2).wisSave(+0).chaSave(-4);
	
		 // Combat Stats
		ac(17).init(-1).hp(142).speed(30).cr(7);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesconstruct); 

		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionsfrightened);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionscharmed);

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Fist(),Fist() ));


        addAction(new Action("Fist", RefList.actiontypesmainaction)
                .addAttack(Fist()));
        
        getCreature().addModifierType(new Regeneration(10));
	}


	private Attack Fist() {
	
        Attack a = new Attack("Fist"); 
        a.setAttackBonus(7) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesbludgeoning);
        a.addDamage().nbrDice(2).dieType(6).damageTypeRefId(RefList.damagetypesforce);
        
        return a;
    }

}

