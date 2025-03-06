/**
 * 
 */
package com.dndcombat.monsters.w;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.DamageBonus;
import com.dndcombat.model.DamageType;
import com.dndcombat.model.DieType;
import com.dndcombat.model.NbrOfDice;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.custom.SpellAction;
import com.dndcombat.monster.custom.SpellWeapon;

/**
 
 */
public class WaterElemental extends Player {
 
	private static final long serialVersionUID = 1L;

	public WaterElemental() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Water Elemental");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Water Elemental.webp");

	    str(18).dex(14).con(18).inte(5).wis(10).cha(8);

		// Saving Throws
		strSave(+4).dexSave(+2).conSave(+4).intSave(-3).wisSave(+0).chaSave(-1);
	
		 // Combat Stats
		ac(14).init(+2).hp(114).speed(30).cr(5);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypeselemental);

		damageResist(RefList.damagetypesfire);
		damageResist(RefList.damagetypesacid);
		damageImmune(RefList.damagetypespoison);
		
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionsrestrained);
		conditionImmune(RefList.conditionsparalyzed);
		conditionImmune(RefList.conditionspetrified);
		conditionImmune(RefList.conditionsprone);
 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Slam(), Slam()));

        addAction(new Action("Slam", RefList.actiontypesmainaction)
                .addAttack(Slam()));
        addAction(Whelm());
	}


	private Attack Slam() {
	
        Attack a = new Attack("Slam"); 
        a.setAttackBonus(7) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .setConditionOnlyAffectsLargeOrSmaller(true)
         .autoApplyCondition(RefList.conditionsprone, new DurationType(10))
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }
	
    private Action Whelm() {
        SpellWeapon bw = SpellWeapon.builder()
                .breathName("Whelm")
                .recharge(4)
                .saveDC(15)
                .saveVsRefId(RefList.savingthrowvsstrength)
                .cube(true)
                .castRangeFeet(0)
                .aoeWidthFeet(10)
                .damage(new DamageModel(new NbrOfDice(4), new DieType(8), new DamageBonus(4), new DamageType(RefList.damagetypesbludgeoning)))
                //TODO push
                .conditionRefId(RefList.conditionsrestrained)
                .build();
        Action a = new SpellAction(bw).createInitialAction();
        return a;
    }

}

