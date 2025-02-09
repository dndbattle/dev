/**
 * 
 */
package com.dndcombat.monsters.i;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.MagicResistance;

/**
 
 */
public class Imp extends Player {
 
	private static final long serialVersionUID = 1L;

	public Imp() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Imp");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Imp.webp");

	    str(6).dex(17).con(13).inte(11).wis(12).cha(14);

		// Saving Throws
		strSave(-2).dexSave(+3).conSave(+1).intSave(+0).wisSave(+1).chaSave(+2);
	
		 // Combat Stats
		ac(13).init(+3).hp(21).speed(20).flySpeed(40).cr(1);

        // Size and Type
	    size(RefList.creaturesizestiny).type(RefList.creaturetypesfiend);

		damageImmune(RefList.damagetypespoison);
		damageImmune(RefList.damagetypesfire);
		conditionImmune(RefList.conditionspoisoned);
		getCreature().addModifierType(new MagicResistance());

		//TODO invis

        addAction(new Action("Sting", RefList.actiontypesmainaction)
                .addAttack(Sting()));
	}


	private Attack Sting() {
	
        Attack a = new Attack("Sting"); 
        a.setAttackBonus(5) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        a.addDamage().nbrDice(2).dieType(6).damageTypeRefId(RefList.damagetypespoison);
        
        return a;
    }

}

