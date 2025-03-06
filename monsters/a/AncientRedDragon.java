/**
 * 
 */
package com.dndcombat.monsters.a;

import java.util.Arrays;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.ConeBreath;
import com.dndcombat.spells.cleric.Command;
import com.dndcombat.spells.wizard.ScorchingRay;

/**
 
 */
public class AncientRedDragon extends Player {
 
	private static final long serialVersionUID = 1L;

	public AncientRedDragon() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Ancient Red Dragon");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Ancient Red Dragon.webp");

	    str(30).dex(10).con(29).inte(18).wis(15).cha(27);

		// Saving Throws
		strSave(+10).dexSave(+7).conSave(+9).intSave(+4).wisSave(+9).chaSave(+8);
	
		 // Combat Stats
		ac(22).init(+14).hp(507).speed(40).flySpeed(80).cr(24);

        // Size and Type
	    size(RefList.creaturesizesgargantuan).type(RefList.creaturetypesdragon);

		getCreature().setLegendaryResistances(4);
		getCreature().setLegendaryActions(3);
		setMonsterSpellDC(23);
		setMonsterSpellAttack(15);

		damageImmune(RefList.damagetypesfire);

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Rend(),Rend(),Rend()));

        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));
        
        addAction(new Action("Fire Breath", RefList.actiontypesmainaction)
                .addAttack(FireBreath()));
        
        addAction(scorchingRays());
        
        addAction(command());
        addAction(new Action("Pounce", RefList.actiontypeslegendaryaction)
                .addAttack(Rend()));
        
        
        
	}
    
    private Action command() {
		Action a = new Action("Command", RefList.actiontypeslegendaryaction);
		a.setMaxUsesPerRound(1);
		a.setMonsterCastAtSlot(2);
		a.setMonsterSpell(new Command());
		return a;
	}

 	private Action scorchingRays() {

		Action a = new Action("Scorching Ray", RefList.actiontypeslegendaryaction);

		a.setMaxUsesPerRound(1);
		a.setMonsterCastAtSlot(3);
		a.setMonsterSpell(new ScorchingRay());

		return a;

	}
 	
	private Attack Rend() {
	
        Attack a = new Attack("Rend"); 
        a.setAttackBonus(17) 
         .setMeleereach(15) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(10) 
         .damageTypeRefId(RefList.damagetypesslashing);
        a.addDamage().nbrDice(3).dieType(6).damageTypeRefId(RefList.damagetypesfire);
        
        return a;
    }
	
	private Attack FireBreath() {


        Attack a = new Attack("Fire Breath"); 

    	BreathWeapon bw = BreathWeapon.builder().actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Fire Breath")
				.recharge(5)
				.shape("Cone")
				.coneOrSphereSizeFeet(90)
				.damage(Arrays.asList(new DamageModel().nbrDice(26).dieType(6).damageTypeRefId(RefList.damagetypesfire)))
				.saveVsRefId(RefList.savingthrowvsdexterity)
				.saveDC(24)
				.build();
    	a.setSpell(new ConeBreath(bw));
 
        return a;

    }

}

