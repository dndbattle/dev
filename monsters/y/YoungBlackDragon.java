/**
 * 
 */
package com.dndcombat.monsters.y;

import java.util.Arrays;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.LineBreath;

/**
 
 */
public class YoungBlackDragon extends Player {
 
	private static final long serialVersionUID = 1L;

	public YoungBlackDragon() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Young Black Dragon");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Young Black Dragon.webp");

	    str(19).dex(14).con(17).inte(12).wis(11).cha(15);

		// Saving Throws
		strSave(+4).dexSave(+5).conSave(+3).intSave(+1).wisSave(+3).chaSave(+2);
	
		 // Combat Stats
		ac(18).init(+5).hp(127).speed(40).flySpeed(80).cr(7);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesdragon);

		damageImmune(RefList.damagetypesacid);

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Rend(),Rend(),Rend() ));
        
        addAction(new Action("Acid Breath", RefList.actiontypesmainaction)
                .addAttack(acidBreath()));

        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));
	}
    
    private Attack acidBreath() {
        // Build the breath weapon
		BreathWeapon bw = BreathWeapon.builder()
				.actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Acid Breath")
				.recharge(5)
				.shape("Line")
				.rangeLengthFeet(30)
				.rangeWidthFeet(5)
				.damage(Arrays.asList(
					new DamageModel()
						.nbrDice(14)
						.dieType(6)
						.damageTypeRefId(RefList.damagetypesacid)
				))
				.saveVsRefId(RefList.savingthrowvsdexterity)
				.saveDC(14)
				.build();

        // Wrap it in an Attack
		Attack a = new Attack("Acid Breath");
		a.setSpell(new LineBreath(bw));
		return a;
	}


	private Attack Rend() {
	
        Attack a = new Attack("Rend"); 
        a.setAttackBonus(7) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(4) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

}

