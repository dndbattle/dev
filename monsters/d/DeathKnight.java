/**
 * 
 */
package com.dndcombat.monsters.d;

import java.util.Arrays;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.map.DnDDistanceCalculator;
import com.dndcombat.fight.model.AdvantageModel;
import com.dndcombat.fight.model.AttackSettings;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.SphereBreath;
import com.dndcombat.monster.actions.WithinXFeetEffect;
import com.dndcombat.monster.traits.MagicResistance;
import com.dndcombat.monster.traits.Parry;
import com.dndcombat.spells.basic.SpellSlot;
import com.dndcombat.spells.cleric.Command;
import com.dndcombat.spells.paladin.DestructiveWave;

/**
 
 */
public class DeathKnight extends Player {
 
	private static final long serialVersionUID = 1L;

	public DeathKnight() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Death Knight");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Death Knight.webp");

	    str(20).dex(11).con(20).inte(12).wis(16).cha(18);

		// Saving Throws
		strSave(+5).dexSave(+6).conSave(+5).intSave(+1).wisSave(+9).chaSave(+4);
	
		 // Combat Stats
		ac(20).init(+12).hp(199).speed(30).cr(17);
		
		setMonsterSpellDC(18);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesundead);

		getCreature().setLegendaryResistances(3);
		getCreature().setLegendaryActions(3);

		damageImmune(RefList.damagetypesnecrotic);
		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionsfrightened);
		conditionImmune(RefList.conditionspoisoned);
		getCreature().addModifierType(new MagicResistance());


        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(DreadBlade(),DreadBlade(),DreadBlade() ));

        addAction(new Action("Dread Blade", RefList.actiontypesmainaction)
                .addAttack(DreadBlade()));
        
        addAction(HellFireOrb());
        
        addAction(destructiveWave());
        
        getCreature().addModifierType(new Parry(6));
        
        getCreature().addModifierType(getMarshalUndead());
        
        addAction(fellWord());
        
        addAction(command());
        
        
        //TODO lunge
        addAction(new Action("Lunge", RefList.actiontypeslegendaryaction)
                .addAttack(DreadBlade()));
        
	}

	private Attack DreadBlade() {
	
        Attack a = new Attack("Dread Blade"); 
        a.setAttackBonus(11) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(5) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        a.addDamage().nbrDice(3).dieType(8).damageTypeRefId(RefList.damagetypesnecrotic);
        
        return a;
    }


	private Action HellFireOrb() {
		Action a = new Action("Hellfire Orb", RefList.actiontypesmainaction);
		BreathWeapon bw =BreathWeapon.builder()
				.breathName(a.getActionName())
				.coneOrSphereSizeFeet(20)
				.recharge(5)
				.saveDC(18)
				.saveVsRefId(RefList.savingthrowvsdexterity)
				.actionTypeRefId(RefList.actiontypesmainaction)
				.damage(Arrays.asList(new DamageModel().nbrDice(10).dieType(6).damageTypeRefId(RefList.damagetypesfire),
						new DamageModel().nbrDice(10).dieType(6).damageTypeRefId(RefList.damagetypesnecrotic)))
				.build();
		a.setSpell(new SphereBreath(bw), new SpellSlot(0));
		
		return a;
	}
	
	private ModifierType getMarshalUndead() {
		return new DefaultModifierType("Marhsal Undead") {
 
			private static final long serialVersionUID = 1L;
		 
			@Override
			public void modifierTypeOnSomeoneAttackAlterAdvantage(Player someone, AdvantageModel a, Player source, Player target, Action action, Attack attack, GameState state, AttackSettings settings) {
				if (someone.isIncapacitated() || someone.isEnemy(source) || someone.isSame(source) || source.getCreature().getCreatureTypeRefId() != RefList.creaturetypesundead) {
					return; // doesnt affect itself or non undead
				}
				if (DnDDistanceCalculator.roughDistanceInSquares(someone, source) > 12) {
					return;
				}
				a.setAdvantage(true);
				a.setAdvantageReason(modifierName);
			}
			
			@Override
			public void modifierTypeOnSomeoneAlterSaveAdvantage(Player someone, AdvantageModel a, Player source, Player meMakingSave, Action action, GameState state) {
				if (someone.isIncapacitated() || someone.isEnemy(meMakingSave) || someone.isSame(meMakingSave) || meMakingSave.getCreature().getCreatureTypeRefId() != RefList.creaturetypesundead) {
					return; // doesnt affect itself or non undead
				}
				if (DnDDistanceCalculator.roughDistanceInSquares(someone, source) > 12) {
					return;
				}
				a.setAdvantage(true);
				a.setAdvantageReason(modifierName);
			}
		};
	}
	

	private Action destructiveWave() {
		Action a = new Action("Destructive Wave", RefList.actiontypesmainaction);
		a.setLimitedUses(2);
		a.setMonsterSpell(new DestructiveWave());
		return a;
	}


	private Action fellWord() {
		Action a = new Action("Fell Word", RefList.actiontypeslegendaryaction);
		a.setMaxNbrUsesPerRound(1);
		
		SaveDC save = new SaveDC();
		save.dc = 18;
		save.saveVsRefId = RefList.savingthrowvsconstitution;
		WithinXFeetEffect w = new WithinXFeetEffect(120, a.getActionName(), Arrays.asList(new DamageModel().nbrDice(5).dieType(6).damageTypeRefId(RefList.damagetypesnecrotic)),
				save, 1);
		w.maxNbrTargets = 1;
		a.setMonsterSpell(w);
		return a;
	}

	private Action command() {
		Action a = new Action("Command", RefList.actiontypeslegendaryaction);
		a.setMaxNbrUsesPerRound(1);
		a.setMonsterSpell(new Command());
		return a;
	}

}

