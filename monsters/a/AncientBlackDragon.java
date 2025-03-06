/**
 * 
 */
package com.dndcombat.monsters.a;

import java.util.Arrays;

import com.dndcombat.actions.InvalidActionException;
import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.DurationType.WhenToEnd;
import com.dndcombat.fight.model.DurationType.WhoseTurn;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.LineBreath;
import com.dndcombat.monster.actions.WithinXFeetEffect;
import com.dndcombat.spells.bard.Fear;
import com.dndcombat.spells.basic.SpellSlot;
import com.dndcombat.spells.wizard.MelfsAcidArrow;

/**
 
 */
public class AncientBlackDragon extends Player {
 
	private static final long serialVersionUID = 1L;

	public AncientBlackDragon() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Ancient Black Dragon");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Ancient Black Dragon.webp");

	    str(27).dex(14).con(25).inte(16).wis(15).cha(22);

		// Saving Throws
		strSave(+8).dexSave(+9).conSave(+7).intSave(+3).wisSave(+9).chaSave(+6);
	
		 // Combat Stats
		ac(22).init(+16).hp(367).speed(40).flySpeed(80).cr(21);

        // Size and Type
		size(RefList.creaturesizesgargantuan).type(RefList.creaturetypesdragon);

		getCreature().setLegendaryResistances(4);
		getCreature().setLegendaryActions(3);
		
		setMonsterSpellDC(21);       // Spell Save DC = 21
		setMonsterSpellAttack(13);   // Spell Attack Bonus = +13

		damageImmune(RefList.damagetypesacid);

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Rend(), Rend(), Rend()));


        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));
        
        addAction(new Action("Acid Breath", RefList.actiontypesmainaction)
                .addAttack(acidBreath()));
        
        addAction(melfsAcidArrow());
        
        addAction(cloudOfInsects());

        // 2) Frightful Presence (casts Fear)
        addAction( frightfulPresence());

        // 3) Pounce (move half speed + Rend)
        addAction(new Action("Pounce", RefList.actiontypeslegendaryaction)
                .addAttack(Rend()));
        
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
        a.setAttackBonus(15) 
         .setMeleereach(15) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(8) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        a.addDamage()
        .nbrDice(2)
        .dieType(8)
        .damageTypeRefId(RefList.damagetypesacid);
        
        return a;
    }
	

 	private Action melfsAcidArrow() {

		Action a = new Action("Melfs Acid Arrow", RefList.actiontypeslegendaryaction);

		a.setMaxUsesPerRound(1);
		a.setMonsterCastAtSlot(4);
		a.setMonsterSpell(new MelfsAcidArrow());

		return a;

	}
	
	  /**
     * Acid Breath
     *  Recharge 5-6
     *  90-foot line, 10 feet wide
     *  Dex save DC 22, 15d8 acid damage
     */
	private Attack acidBreath() {
        // Build the breath weapon
		BreathWeapon bw = BreathWeapon.builder()
				.actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Acid Breath")
				.recharge(5)
				.shape("Line")
				.rangeLengthFeet(90)
				.rangeWidthFeet(10)
				.damage(Arrays.asList(
					new DamageModel()
						.nbrDice(15)
						.dieType(8)
						.damageTypeRefId(RefList.damagetypesacid)
				))
				.saveVsRefId(RefList.savingthrowvsdexterity)
				.saveDC(22)
				.build();

        // Wrap it in an Attack
		Attack a = new Attack("Acid Breath");
		a.setSpell(new LineBreath(bw));
		return a;
	}

    /**
     * Legendary Action: Cloud of Insects
     *  Dex save DC 21, 6d10 poison on failure
     *  Disadvantage on Concentration saves until end of target's next turn (on fail).
     */
	private Action cloudOfInsects() {
		Action a = new Action("Cloud of Insects", RefList.actiontypeslegendaryaction);
		a.setMaxUsesPerRound(1);
		SaveDC save = new SaveDC();
		save.dc = 21;
		save.saveVsRefId = RefList.savingthrowvsdexterity;
		//
		DurationType duration = new DurationType(1);
		duration.whenToEnd = WhenToEnd.EndOfTurn;
		duration.whoseTurn = WhoseTurn.TargetTurn;
		WithinXFeetEffect w = new WithinXFeetEffect(120, a.getActionName(), 
				Arrays.asList(new DamageModel().nbrDice(6).dieType(10).damageTypeRefId(RefList.damagetypespoison)
						.autoApplyCondition(RefList.conditionsconcentrationdisadvonsave, duration)
						),
				save, 1);
		w.maxNbrTargets = 1;

		a.setSpell(w, new SpellSlot(0));
		return a;
	}

	/**
	 * Legendary Action: Frightful Presence that effectively casts Fear
	 * If your engine supports a direct Spell object, link that in. 
	 */
	private Action frightfulPresence() {
		Action action = new Action("Frightful Presence", RefList.actiontypeslegendaryaction);
		action.setMonsterSpell(new Fear());
		return action;
	}


}

