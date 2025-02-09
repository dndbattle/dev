/**
 * 
 */
package com.dndcombat.monsters.d;

import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.PackTactics;

/**
 
 */
public class DireWolf extends Player {
 
	private static final long serialVersionUID = 1L;

	public DireWolf() {
		super(new Creature());
		create();
    }
   
    /**
     */
    public void create() {
    	getCreature().setCreatureName("Dire Wolf");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Dire Wolf.webp");

	    str(17).dex(15).con(15).inte(3).wis(12).cha(7);

		// Saving Throws
		strSave(3).dexSave(2).conSave(2).intSave(-4).wisSave(1).chaSave(-2);
	
		 // Combat Stats
		ac(14).init(+2).hp(22).speed(50).cr(1);
 
        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesbeast);


        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));
        
        getCreature().addModifierType(new PackTactics());
	}


	private Attack Bite() {
	
        Attack a = new Attack("Bite"); 
        a.setAttackBonus(5) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(10) 
         .bonus(3) 
         .autoApplyCondition(RefList.conditionsprone, new DurationType(10), true)
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }

}

