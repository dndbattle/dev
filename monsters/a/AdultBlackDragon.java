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
 * 
 */
public class AdultBlackDragon extends Player {
 
	private static final long serialVersionUID = 1L;

	public AdultBlackDragon() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Adult Black Dragon");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Adult Black Dragon.webp");

	    str(23).dex(14).con(21).inte(14).wis(13).cha(19);

		// Saving Throws
		strSave(+6).dexSave(+7).conSave(+5).intSave(+2).wisSave(+6).chaSave(+4);
	
		 // Combat Stats
		ac(19).init(+12).hp(195).speed(40).flySpeed(80).cr(14);

        // Size and Type
	    size(RefList.creaturesizeshuge).type(RefList.creaturetypesdragon);

		getCreature().setLegendaryResistances(3);
		getCreature().setLegendaryActions(3);
		
		setMonsterSpellDC(17);       // Spell Save DC = 17
		setMonsterSpellAttack(+9);   // Spell Attack Bonus = +9

		damageImmune(RefList.damagetypesacid);

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Rend(), Rend(), Rend()));

        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));
        
        addAction(new Action("Acid Breath", RefList.actiontypesmainaction)
                .addAttack(acidBreath()));
        
        addAction(melfsAcidArrow());
        
        addAction(cloudOfInsects());

        // 2) Frightful Presence (casts Fear)
        addAction(frightfulPresence());

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
        a.setAttackBonus(11) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(6) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        a.addDamage()
        .nbrDice(1)
        .dieType(8)
        .damageTypeRefId(RefList.damagetypesacid);
        
        return a;
    }
	

 	private Action melfsAcidArrow() {

		Action a = new Action("Melfs Acid Arrow", RefList.actiontypeslegendaryaction);

		a.setMaxUsesPerRound(1);
		a.setMonsterCastAtSlot(3);
		a.setMonsterSpell(new MelfsAcidArrow());

		return a;

	}
	
	  /**
     * Acid Breath
     *  Recharge 5-6
     *  60-foot line, 5 feet wide
     *  Dex save DC 18, 12d8 acid damage
     */
	private Attack acidBreath() {
        // Build the breath weapon
		BreathWeapon bw = BreathWeapon.builder()
				.actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Acid Breath")
				.recharge(5)
				.shape("Line")
				.rangeLengthFeet(60)
				.rangeWidthFeet(5)
				.damage(Arrays.asList(
					new DamageModel()
						.nbrDice(12)
						.dieType(8)
						.damageTypeRefId(RefList.damagetypesacid)
				))
				.saveVsRefId(RefList.savingthrowvsdexterity)
				.saveDC(18)
				.build();

        // Wrap it in an Attack
		Attack a = new Attack("Acid Breath");
		a.setSpell(new LineBreath(bw));
		return a;
	}

    /**
     * Legendary Action: Cloud of Insects
     *  Dex save DC 17, 4d10 poison on failure
     *  Disadvantage on Concentration saves until end of target's next turn (on fail).
     */
	private Action cloudOfInsects() {
		Action a = new Action("Cloud of Insects", RefList.actiontypeslegendaryaction);
		a.setMaxUsesPerRound(1);
		SaveDC save = new SaveDC();
		save.dc = 17;
		save.saveVsRefId = RefList.savingthrowvsdexterity;
		//
		DurationType duration = new DurationType(1);
		duration.whenToEnd = WhenToEnd.EndOfTurn;
		duration.whoseTurn = WhoseTurn.TargetTurn;
		WithinXFeetEffect w = new WithinXFeetEffect(120, a.getActionName(), 
				Arrays.asList(new DamageModel().nbrDice(4).dieType(10).damageTypeRefId(RefList.damagetypespoison)
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