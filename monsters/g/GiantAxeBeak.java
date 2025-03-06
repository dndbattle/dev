/**
 * 
 */
package com.dndcombat.monsters.g;

import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class GiantAxeBeak extends Player {
 
	private static final long serialVersionUID = 1L;

	public GiantAxeBeak() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Giant Axe Beak");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Axe Beak.webp");

	    str(21).dex(14).con(19).inte(3).wis(12).cha(5);

		// Saving Throws
		strSave(+5).dexSave(+2).conSave(+4).intSave(-4).wisSave(+1).chaSave(-3);
	
		 // Combat Stats
		ac(15).init(+5).hp(84).speed(50).cr(5);

        // Size and Type
	    size(RefList.creaturesizeshuge).type(RefList.creaturetypesmonstrosity);

 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( SharpenedBeak(), Talons()));


        addAction(new Action("Sharpened Beak", RefList.actiontypesmainaction)
                .addAttack(SharpenedBeak()));

        addAction(new Action("Talons", RefList.actiontypesmainaction)
                .addAttack(Talons()));
	}


	private Attack SharpenedBeak() {
	
        Attack a = new Attack("Sharpened Beak"); 
        a.setAttackBonus(8) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(12) 
         .bonus(5) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

	private Attack Talons() {
	
        Attack a = new Attack("Talons"); 
        a.setAttackBonus(8) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage()
         .autoApplyCondition(RefList.conditionsprone, new DurationType(10))
         .setConditionOnlyAffectsLargeOrSmaller(true)
         .nbrDice(2) 
         .dieType(8) 
         .bonus(5) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }

}

