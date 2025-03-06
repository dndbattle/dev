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
import com.dndcombat.monster.actions.ConeBreath;

/**
 
 */
public class YoungWhiteDragon extends Player {
 
	private static final long serialVersionUID = 1L;

	public YoungWhiteDragon() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Young White Dragon");
//   
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Young White Dragon.webp");

	    str(18).dex(10).con(18).inte(6).wis(11).cha(12);

		// Saving Throws
		strSave(+4).dexSave(+3).conSave(+4).intSave(2).wisSave(+3).chaSave(+1);
	
		 // Combat Stats
		ac(17).init(+3).hp(123).speed(40).flySpeed(80).cr(6);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesdragon);

		damageImmune(RefList.damagetypescold);
	 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Rend(),Rend(),Rend()));

        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));
        
        addAction(new Action("Cold Breath", RefList.actiontypesmainaction)
                .addAttack(ColdBreath()));

	}

    
    private Attack ColdBreath() {
        // Build the breath weapon
		BreathWeapon bw = BreathWeapon.builder()
				.actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Cold Breath")
				.recharge(5)
				.shape("Cone")
				.coneOrSphereSizeFeet(30)
				.damage(Arrays.asList(
					new DamageModel()
						.nbrDice(9)
						.dieType(8)
						.damageTypeRefId(RefList.damagetypescold)
				))
				.saveVsRefId(RefList.savingthrowvsconstitution)
				.saveDC(15)
				.build();

        // Wrap it in an Attack
		Attack a = new Attack("Cold Breath");
		a.setSpell(new ConeBreath(bw));
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
        a.addDamage().nbrDice(1).dieType(4).damageTypeRefId(RefList.damagetypescold);
        
        return a;
    }

}

