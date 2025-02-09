/**
 * 
 */
package com.dndcombat.monsters.k;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.PackTactics;

/**
 
 */
public class KoboldWarrior extends Player {
 
	private static final long serialVersionUID = 1L;

	public KoboldWarrior() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Kobold Warrior");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Kobold.webp");

	    str(7).dex(15).con(9).inte(8).wis(7).cha(8);

		// Saving Throws
		strSave(-2).dexSave(+2).conSave(-1).intSave(-1).wisSave(-2).chaSave(-1);
	
		 // Combat Stats
		ac(14).init(+2).hp(7).speed(30).cr(0.125);

        // Size and Type
	    size(RefList.creaturesizessmall).type(RefList.creaturetypesdragon);


       addAction(new Action("Dagger", RefList.actiontypesmainaction)
                .addAttack(Dagger()));


       addAction(new Action("Dagger Thrown", RefList.actiontypesmainaction)
                .addAttack(DaggerThrown()));

       getCreature().addModifierType(new PackTactics());
       //TODO in sunlight?
	}

    private Attack Dagger() {
        Attack a = new Attack("Dagger"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(4) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        return a;
    }
    private Attack DaggerThrown() {
        Attack a = new Attack("Dagger Thrown"); 
        a.setAttackBonus(4) 
         .setAttackRange(20) // 0 if not found
         .setLongRange(60) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(4) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        return a;
    }

}

