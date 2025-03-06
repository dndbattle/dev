/**
 * 
 */
package com.dndcombat.monsters.c;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.actions.Dominate;

/**
 
 */
public class Cambion extends Player {
 
	private static final long serialVersionUID = 1L;

	public Cambion() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Cambion");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Cambion.webp");

	    str(18).dex(18).con(16).inte(14).wis(12).cha(16);

		// Saving Throws
		strSave(+7).dexSave(+4).conSave(+6).intSave(+5).wisSave(+1).chaSave(+6);
		setMonsterSpellDC(14);
	
		 // Combat Stats
		ac(19).init(+4).hp(105).speed(30).cr(5);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesfiend);
	    
	    addAction(new Action("Claw", RefList.actiontypesmainaction)
	            .addAttack(Claw()));
    
	    addAction(new Action("Fire Ray", RefList.actiontypesmainaction)
	    		.addAttack(FireRay()));
	    
	    addAction(new Action("Dominate Person", RefList.actiontypesmainaction).setLimitedUses(1)
	    		.addAttack(dominatePerson()));
	    
	    setMoveCloserAfterRound(1);
	}

	private Attack dominatePerson() {
		Attack enslave = new Attack("Dominate Person");
		enslave.setSpell(new Dominate(enslave.getAttackName(), 14, true));
		return enslave;
	}

    private Attack FireRay() {
        Attack a = new Attack("Fire Ray"); 
        a.setAttackBonus(7) 
         .setAttackRange(120) 
         .setLongRange(0) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .dieType(6) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypesfire); 
        return a;
    }

	private Attack Claw() {
	
	    Attack a = new Attack("Claw"); 
	    a.setAttackBonus(7) 
	     .setMeleereach(5) 
	     .setRollToHitInd(1) 
	     .getFirstDamage() 
	     .nbrDice(1) 
	     .dieType(8) 
	     .bonus(4) 
	     .damageTypeRefId(RefList.damagetypesslashing);
	    a.addDamage().nbrDice(2).dieType(6).damageTypeRefId(RefList.damagetypesfire);
	    
	    return a;
	}

}

