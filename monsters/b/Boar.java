/**
 * 
 */
package com.dndcombat.monsters.b;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.actions.ChargeDamage;

/**
 
 */
public class Boar extends Player {
 
	private static final long serialVersionUID = 1L;

	public Boar() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Boar");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Boar.webp");

	    str(13).dex(11).con(14).inte(2).wis(9).cha(5);

		// Saving Throws
		strSave(1).dexSave(0).conSave(2).intSave(-4).wisSave(-1).chaSave(-3);
	
		 // Combat Stats
		ac(11).init(+0).hp(13).speed(40).cr(0.125);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesbeast);

        // Actions
        addAction(new Action("Gore", RefList.actiontypesmainaction)
                .addAttack(Gore()));
   
        DamageModel d = new DamageModel().nbrDice(1).dieType(6).autoApplyCondition(RefList.conditionsprone, new DurationType(10));
		getCreature().getModifierTypes().add(new ChargeDamage(d, 20));

    }
    

	private Attack Gore() {
		
        Attack a = new Attack("Gore");
        a.setAttackBonus(3)
         .setMeleereach(5)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(1)
         .dieType(6)
         .bonus(1)
         .damageTypeRefId(RefList.damagetypespiercing);
        
        return a;
    }
	
}
