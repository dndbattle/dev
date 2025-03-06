/**
 * 
 */
package com.dndcombat.monsters.c;

import java.util.Arrays;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.LineBreath;
import com.dndcombat.monster.traits.MagicResistance;

/**
 
 */
public class Colossus extends Player {
 
	private static final long serialVersionUID = 1L;

	public Colossus() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Colossus");
//   
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MOT/Colossus+of+Akros.webp");

	    str(30).dex(11).con(30).inte(3).wis(11).cha(8);

		// Saving Throws
		strSave(+10).dexSave(+8).conSave(+10).intSave(-4).wisSave(+8).chaSave(-1);
	
		 // Combat Stats
		ac(23).init(+16).hp(553).speed(60).cr(25);

        // Size and Type
	    size(RefList.creaturesizesgargantuan).type(RefList.creaturetypesconstruct);

		getCreature().setLegendaryResistances(4);

		getCreature().setLegendaryActions(4);

		damageResist(RefList.damagetypesnecrotic);
		damageResist(RefList.damagetypesradiant);
		damageImmune(RefList.damagetypespoison);
		damageImmune(RefList.damagetypespsychic);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionsfrightened);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionscharmed);
		conditionImmune(RefList.conditionsparalyzed);
		conditionImmune(RefList.conditionspetrified);
		conditionImmune(RefList.conditionsstunned);
		getCreature().addModifierType(new MagicResistance());

 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Slam(),Slam(),Slam() ));

        addAction(new Action("Slam", RefList.actiontypesmainaction)
                .addAttack(Slam()));

        addAction(new Action("Radiant Ray", RefList.actiontypesmainaction)
                .addAttack(RadiantRay()));

        addAction(new Action("Smite", RefList.actiontypeslegendaryaction)
                .addAttack(RadiantRay()));

        //TODO chance to move up and hit a healer for instance
        addAction(new Action("Stomp", RefList.actiontypeslegendaryaction)
                .addAttack(Slam()));
        
        addAction(new Action("Divine Beam", RefList.actiontypesmainaction) 
        		.addAttack(divineBeam()));
	}
    
    /**
      
     */
	private Attack divineBeam() {
        // Build the breath weapon
		BreathWeapon bw = BreathWeapon.builder()
				.actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Divine Beam")
				.recharge(5)
				.shape("Line")
				.rangeLengthFeet(300)
				.rangeWidthFeet(10)
				.damage(Arrays.asList(
					new DamageModel()
						.nbrDice(10)
						.dieType(12)
						.damageTypeRefId(RefList.damagetypesradiant)
				))
				.saveVsRefId(RefList.savingthrowvsdexterity)
				.saveDC(26)
				.build();

        // Wrap it in an Attack
		Attack a = new Attack("Divine Beam");
		a.setSpell(new LineBreath(bw));
		return a;
	}

	private Attack Slam() {
		//TODO pushes 20 feet
        Attack a = new Attack("Slam"); 
        a.setAttackBonus(18) 
         .setMeleereach(20) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(4) 
         .dieType(10) 
         .bonus(10) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }

	private Attack RadiantRay() {
	
        Attack a = new Attack("Radiant Ray"); 
        a.setAttackBonus(18) 
         .setMeleereach(20) 
         .setRollToHitInd(1) 
         .getFirstDamage()
         .nbrDice(4) 
         .dieType(10) 
         .setConditionOnlyAffectsLargeOrSmaller(true)
         .autoApplyCondition(RefList.conditionsprone, new DurationType(10))
         .damageTypeRefId(RefList.damagetypesradiant); 
        
        return a;
    }

}

