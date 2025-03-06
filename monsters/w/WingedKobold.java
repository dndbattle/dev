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
public class WingedKobold extends Player {
 
	private static final long serialVersionUID = 1L;

	public WingedKobold() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Winged Kobold");
//   
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Winged Kobold.webp");

	    str(7).dex(16).con(9).inte(8).wis(7).cha(8);

		// Saving Throws
		strSave(-2).dexSave(+3).conSave(-1).intSave(-1).wisSave(-2).chaSave(-1);
	
		 // Combat Stats
		ac(15).init(+3).hp(10).speed(30).flySpeed(30).cr(0.25);

        // Size and Type
	    size(RefList.creaturesizessmall).type(RefList.creaturetypesdragon);

        addAction(new Action("Dragon-Tooth Blade", RefList.actiontypesmainaction)
                .addAttack(DragonToothBlade()));

        addAction(new Action("Chromatic Spittle", RefList.actiontypesmainaction)
                .addAttack(ChromaticSpittle()));
        
        getCreature().addModifierType(new PackTactics());
	}


	private Attack DragonToothBlade() {
	
        Attack a = new Attack("Dragon-Tooth Blade"); 
        a.setAttackBonus(5) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }
	
	private Attack ChromaticSpittle() {
	
        Attack a = new Attack("Chromatic Spittle"); 
        a.setAttackBonus(5) 
         .setAttackRange(30) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypesacid); 
        
        return a;
    }

}

