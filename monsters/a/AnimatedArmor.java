/**
 * 
 */
package com.dndcombat.monsters.a;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class AnimatedArmor extends Player {
 
	private static final long serialVersionUID = 1L;

	public AnimatedArmor() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Animated Armor");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Animated Armor.webp");

	    str(14).dex(11).con(13).inte(1).wis(3).cha(1);

		// Saving Throws
		strSave(+2).dexSave(+0).conSave(+1).intSave(-5).wisSave(-4).chaSave(-5);
	
		 // Combat Stats
		ac(18).init(+2).hp(33).speed(25).cr(1);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesconstruct);

		damageImmune(RefList.damagetypespoison);
		damageImmune(RefList.damagetypespsychic);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionsfrightened);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionscharmed);
		conditionImmune(RefList.conditionsparalyzed);
		conditionImmune(RefList.conditionspetrified);
	 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Slam(),Slam() ));

        addAction(new Action("Slam", RefList.actiontypesmainaction)
                .addAttack(Slam()));
	}


	private Attack Slam() {
	
        Attack a = new Attack("Slam"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }

}

