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
public class WarriorInfantry extends Player {
 
	private static final long serialVersionUID = 1L;

	public WarriorInfantry() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Warrior Infantry");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Veteran.webp");

	    str(13).dex(11).con(11).inte(8).wis(11).cha(8);

		// Saving Throws
		strSave(+1).dexSave(+0).conSave(+0).intSave(-1).wisSave(+0).chaSave(-1);
	
		 // Combat Stats
		ac(13).init(+0).hp(9).speed(30).cr(0.125);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);
	    
	    getCreature().addModifierType(new PackTactics());
 
       addAction(new Action("Spear", RefList.actiontypesmainaction)
                .addAttack(Spear()));


       addAction(new Action("Spear Thrown", RefList.actiontypesmainaction)
                .addAttack(SpearThrown()));

	}

    private Attack Spear() {
        Attack a = new Attack("Spear"); 
        a.setAttackBonus(3) 
         .setMeleereach(5) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        return a;
    }
    private Attack SpearThrown() {
        Attack a = new Attack("Spear Thrown"); 
        a.setAttackBonus(3) 
         .setAttackRange(20) // 0 if not found
         .setLongRange(60) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        return a;
    }

}

