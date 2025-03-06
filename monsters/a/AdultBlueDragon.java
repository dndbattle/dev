/**
 * 
 */
package com.dndcombat.monsters.a;

import java.util.Arrays;

import com.dndcombat.actions.InvalidActionException;
import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.LineBreath;
import com.dndcombat.spells.bard.Shatter;

/**
 * 
 */
public class AdultBlueDragon extends Player {
 
	private static final long serialVersionUID = 1L;

	public AdultBlueDragon() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Adult Blue Dragon");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Adult Blue Dragon.webp");

	    str(25).dex(10).con(23).inte(16).wis(15).cha(20);

		// Saving Throws
		strSave(+7).dexSave(+5).conSave(+6).intSave(+3).wisSave(+7).chaSave(+5);
	
		 // Combat Stats
		ac(19).init(+10).hp(212).speed(40).flySpeed(80).cr(16);

        // Size and Type
	    size(RefList.creaturesizeshuge).type(RefList.creaturetypesdragon);

		getCreature().setLegendaryResistances(3);
		getCreature().setLegendaryActions(3);
		
		setMonsterSpellDC(18);       // Spell Save DC = 18
		setMonsterSpellAttack(10);   // Spell Attack Bonus = +10

		damageImmune(RefList.damagetypeslightning);

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Rend(), Rend(), Rend()));

        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));
        
        addAction(new Action("Lightning Breath", RefList.actiontypesmainaction)
                .addAttack(lightningBreath()));
        
        addAction(new Action("Tail Swipe", RefList.actiontypeslegendaryaction)
                .addAttack(Rend()));
     
        addAction(sonicBoom());
        
        getCreature().addModifierType(new DefaultModifierType("Strategy") {
 
			private static final long serialVersionUID = 1L;
        	@Override
        	public int evaluateScore(Player me, Player target, Action action, long actionTypeRefId, GameState state) throws InvalidActionException {
        		return 0;
        	}
        });
	}

	private Attack Rend() {
	
        Attack a = new Attack("Rend"); 
        a.setAttackBonus(12) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(7) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        a.addDamage()
        .nbrDice(1)
        .dieType(10)
        .damageTypeRefId(RefList.damagetypeslightning);
        
        return a;
    }

    /**
     * Lightning Breath
     *  Recharge 5-6
     *  90-foot line, 5 feet wide
     *  Dex save DC 19, 11d10 lightning damage
     */
	private Attack lightningBreath() {
        // Build the breath weapon
		BreathWeapon bw = BreathWeapon.builder()
				.actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Lightning Breath")
				.recharge(5)
				.shape("Line")
				.rangeLengthFeet(90)
				.rangeWidthFeet(5)
				.damage(Arrays.asList(
					new DamageModel()
						.nbrDice(11)
						.dieType(10)
						.damageTypeRefId(RefList.damagetypeslightning)
				))
				.saveVsRefId(RefList.savingthrowvsdexterity)
				.saveDC(19)
				.build();

        // Wrap it in an Attack
		Attack a = new Attack("Lightning Breath");
		a.setSpell(new LineBreath(bw));
		return a;
	}

   

    /**
     * Legendary Action: Sonic Boom
     *  The dragon uses Spellcasting to cast Shatter.
     */
	private Action sonicBoom() {
		Action a = new Action("Shatter", RefList.actiontypeslegendaryaction);
		a.setMaxUsesPerRound(1);
		a.setMonsterSpell(new Shatter());
		return a;
	}
}