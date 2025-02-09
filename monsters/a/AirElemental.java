/**
 * 
 */
package com.dndcombat.monsters.a;


import com.dndcombat.creatures.model.DamageModel;
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
 * 
 */

public class AirElemental extends Player {

    private static final long serialVersionUID = 1L;

    public AirElemental() {
		super(new Creature());
		
        getCreature().setCreatureName("Air Elemental");
        setPlayersSide(false);
        getCreature().setImageUrl("/assets/monsters/MM/Air Elemental.webp");
        

	    // Ability Scores
        str(14).dex(20).con(14).inte(6).wis(10).cha(6);

        // Saving Throws
        strSave(2).dexSave(5).conSave(2).intSave(-2).wisSave(0).chaSave(-2);

        // Combat Stats
        ac(15).init(5).hp(90).speed(10).flySpeed(90).cr(5);

        // Size and Type
        size(RefList.creaturesizeslarge).type(RefList.creaturetypeselemental);
        
		addAction(new Action("Multiattack", RefList.actiontypesmainaction)
				.addAttack(ThunderousSlam(), ThunderousSlam()));
		
		addAction(new Action("Thunderous Slam", RefList.actiontypesmainaction)
				.addAttack(ThunderousSlam()));
		addAction(Whirlwind());

    }

    private Action Whirlwind() {
        SpellWeapon bw = SpellWeapon.builder()
                .breathName("Whirlwind")
                .recharge(4)
                .saveDC(13)
                .saveVsRefId(RefList.savingthrowvsstrength)
                .cube(true)
                .castRangeFeet(0)
                .aoeWidthFeet(10)
                .damage(new DamageModel(new NbrOfDice(4), new DieType(10), new DamageBonus(2), new DamageType(RefList.damagetypesthunder)))
                //TODO push
                .conditionRefId(RefList.conditionsprone)
                .build();
        Action a = new SpellAction(bw).createInitialAction();
        return a;
    }

	private Attack ThunderousSlam() {
		Attack a = new Attack("Thunderous Slam");
		a.setAttackBonus(8).setMeleereach(10).setRollToHitInd(1)
		//.setGrappleOnHit(true).setGrappleDc(14).setGrappleLargeOrSmaller(true)
		.getFirstDamage().nbrDice(2).dieType(8).bonus(5).damageTypeRefId(RefList.damagetypesthunder);
		return a;
	}

}