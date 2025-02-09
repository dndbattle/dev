/**
 * 
 */
package com.dndcombat.monsters.a;

import java.util.ArrayList;
import java.util.List;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.DamageType;
import com.dndcombat.model.DieType;
import com.dndcombat.model.NbrOfDice;
import com.dndcombat.model.RefList;
import com.dndcombat.spells.basic.SpellSlot;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.LineBreath;

/**
 
 */
public class Ankheg extends Player {
 
	private static final long serialVersionUID = 1L;

	public Ankheg() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Ankheg");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Ankheg.webp");

        // Ability Scores
        str(17).dex(11).con(14).inte(1).wis(13).cha(6);

        // Saving Throws
        strSave(3).dexSave(0).conSave(2).intSave(-5).wisSave(15).chaSave(-2);

        // Combat Stats
        ac(14).init(0).hp(45).speed(30).cr(2);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesmonstrosity);
  
        // Actions
        // Add the Ankheg's two actions.
        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));
        addAction(AcidSpray());
        
        //setMonsterSpellDC(13);
        //addAction(LightningBolt());
        // delay moving closer
        //setMoveCloserAfterRound(2);
    }
    

	private Attack Bite() {
        Attack a = new Attack("Bite");
        a.setAttackBonus(5)
         .setMeleereach(5)
         .setRollToHitInd(1)
         .setGrappleOnHit(true)
         .setGrappleDc(13)
         .setGrappleLargeOrSmaller(true)
         .getFirstDamage()
         .nbrDice(2)
         .dieType(6)
         .bonus(3)
         .damageTypeRefId(RefList.damagetypesslashing);
        a.addDamage()
        .nbrDice(1)
        .dieType(6)
        .damageTypeRefId(RefList.damagetypesacid);
        
        return a;
    }
	
	private Action AcidSpray() {
		Action acidSpray = new Action("Acid Spray", RefList.actiontypesmainaction);
		List<DamageModel> damage = new ArrayList<DamageModel>();
		damage.add(new DamageModel(new NbrOfDice(3), new DieType(6), new DamageType(RefList.damagetypesacid)));
		BreathWeapon bw = BreathWeapon.builder().actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Acid Spray")
				.useWhileGrappling(false)
				.recharge(6)
				.rangeLengthFeet(30)
				.rangeWidthFeet(5)
				.damage(damage)
				.saveVsRefId(RefList.savingthrowvsdexterity)
				.saveDC(13).build();
		
		acidSpray.setSpell(new LineBreath(bw), new SpellSlot(0));
		return acidSpray;
	}
}
