/**
 * 
 */
package com.dndcombat.monsters.h;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.PackTactics;

/**
 
 */
public class HobgoblinWarrior extends Player {
 
	private static final long serialVersionUID = 1L;

	public HobgoblinWarrior() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Hobgoblin Warrior");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Hobgoblin.webp");

	    str(13).dex(12).con(12).inte(10).wis(10).cha(9);

		// Saving Throws
		strSave(+1).dexSave(+1).conSave(+1).intSave(+0).wisSave(+0).chaSave(-1);
	
		 // Combat Stats
		ac(18).init(+3).hp(11).speed(30).cr(0.5);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesgoblinoid);


        addAction(new Action("Longsword", RefList.actiontypesmainaction)
                .addAttack(Longsword()));
        addAction(new Action("Longbow", RefList.actiontypesmainaction)
                .addAttack(Longbow()));
        getCreature().addModifierType(new PackTactics());
        
        setMoveCloserAfterRound(1);
	}


	private Attack Longsword() {
	
        Attack a = new Attack("Longsword"); 
        a.setAttackBonus(3) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(10) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }
    private Attack Longbow() {
        Attack a = new Attack("Longbow"); 
        a.setAttackBonus(3) 
         .setAttackRange(150) 
         .setLongRange(600) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(8) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypespiercing);
        a.addDamage().nbrDice(3).dieType(4).damageTypeRefId(RefList.damagetypespoison);
        return a;
    }

}

