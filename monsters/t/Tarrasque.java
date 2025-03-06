/**
 * 
 */
package com.dndcombat.monsters.t;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dndcombat.actions.DamageManager;
import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.conditions.Blinded;
import com.dndcombat.fight.conditions.Restrained;
import com.dndcombat.fight.conditions.Untargetable;
import com.dndcombat.fight.model.AttackResult;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Modifier;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.model.ValidTargetResults;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.Damage;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.ConeBreath;
import com.dndcombat.monster.traits.MagicResistance;
import com.dndcombat.savingthrows.SaveResult;
import com.dndcombat.util.Converter;
import com.dndcombat.util.Roll;
import com.dndcombat.util.RollResult;

/**
 
 */
public class Tarrasque extends Player {
 
	private static final long serialVersionUID = 1L;

	public Tarrasque() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Tarrasque");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Tarrasque.webp");

	    str(30).dex(11).con(30).inte(3).wis(11).cha(11);

		// Saving Throws
		strSave(+10).dexSave(+9).conSave(+10).intSave(+5).wisSave(+9).chaSave(+9);
	
		 // Combat Stats
		ac(25).init(+18).hp(697).speed(60).cr(30);

        // Size and Type
	    size(RefList.creaturesizesgargantuan).type(RefList.creaturetypesmonstrosity);

		damageImmune(RefList.damagetypespoison);
		damageImmune(RefList.damagetypesfire);
		conditionImmune(RefList.conditionsfrightened);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionscharmed);
		getCreature().addModifierType(new MagicResistance());


        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Bite(), Claw(), Claw(), Tail() ));


        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));

        addAction(new Action("Claw", RefList.actiontypesmainaction)
                .addAttack(Claw()));

        addAction(new Action("Tail", RefList.actiontypesmainaction)
                .addAttack(Tail()));
        
        addAction(new Action("Onslaught", RefList.actiontypeslegendaryaction)
        		.addAttack(Tail()));
        
        addAction(new Action("Onslaught", RefList.actiontypeslegendaryaction)
        		.addAttack(Claw()));
        //TODO world-shaking movement
        
        addAction(new Action("Thunderous Bellow", RefList.actiontypesmainaction)
        		.addAttack(ThunderousBellow()));
        
		getCreature().getModifierTypes().add(ReflectiveCarapace());

        getCreature().addModifierType(Swallow());
	}
    
    
    private ModifierType Swallow() {
		return new DefaultModifierType("Swallow") {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target, List<Damage> list, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
				if (Converter.isTrimmedSameIgnoreCase(attack.getAttackName(), "Bite")) {
					SaveDC save = new SaveDC();
					save.saveVsRefId = RefList.savingthrowvsstrength;
					save.dc = 27;
					SaveResult sr = target.makeSavingThrow(save, action, source, state);
					if (sr.passed) {
						state.addActionLog(source.getName() + " avoids being swallowed, " + sr.getRollDisplay());
						return false;
					}
					 if (source.isGrappling(target) && source.hasBonusAction()) {
						state.addActionLog(source.getName() + " swallows " + target.getName()  +", " + sr.getRollDisplay());
						source.useBonusAction();
						//TODO track swallowed fight dmg from the inside
						Modifier swallowed = new Modifier("Swallowed", new DefaultModifierType("Swallowed") {

							
							private static final long serialVersionUID = 1L;
							@Override
							public boolean applyModifierOnMeStartOfMySomeonesTurnRemoveIfTrue(Player someone, Player myturn, Modifier m, GameState state) {
								if (someone.isSame(target) && myturn.isSame(source)) {
									// modifier is on someone and its the tarrasque turn
									state.addActionLog(target.getName() + " takes stomache acid damage");
									List<Damage> damage = new ArrayList<Damage>();
									Damage d= new Damage();
									RollResult rr = Roll.nd(16,6);
									d.dmg = rr.getTotal();
									d.diceDisplay = rr.getDisplay();
									d.damageTypeRefId = RefList.damagetypesacid;
									damage.add(d);
									new DamageManager().applyDamage(source, action, attack, target, damage, state);
								}
								return false;
							}
							
						}, source, action, new DurationType(10), state);
						swallowed.setIncludesConditions(Arrays.asList(Blinded.class, Restrained.class, Untargetable.class));
						target.addModifier(state,swallowed);
					}
				}
				return false;
			}
		};
	}



    private Attack ThunderousBellow() {
    	
        Attack a = new Attack("Thunderous Bellow"); 
    	BreathWeapon bw = BreathWeapon.builder().actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Thunderous Bellow")
				.recharge(5)
				.shape("Cone")
				.coneOrSphereSizeFeet(150)
				.damage(Arrays.asList(new DamageModel().nbrDice(12).dieType(12)
						.damageTypeRefId(RefList.damagetypesthunder)
						.autoApplyCondition(RefList.conditionsfrightened, new DurationType(10))
						))
				.saveVsRefId(RefList.savingthrowvsconstitution)
				.saveDC(27)
				.build();
    	a.setSpell(new ConeBreath(bw));
        
        return a;
    }
	

	private Attack Bite() {
	
        Attack a = new Attack("Bite"); 
        a.setAttackBonus(19) 
         .setMeleereach(15) 
         .setRollToHitInd(1) 
         .setGrappleOnHit(true)
         .setGrappleIsRestrained(true)
         .setGrappleDc(20)
         .getFirstDamage() 
         .nbrDice(4) 
         .dieType(12) 
         .bonus(10) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }

	private Attack Claw() {
	
        Attack a = new Attack("Claw"); 
        a.setAttackBonus(19) 
         .setMeleereach(15) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(4) 
         .dieType(8) 
         .bonus(10) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

	private Attack Tail() {
	
        Attack a = new Attack("Tail"); 
        a.setAttackBonus(19) 
         .setMeleereach(30) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .dieType(8) 
         .bonus(10) 
         .autoApplyCondition(RefList.conditionsprone, new DurationType(10)) //TODO huge or smaller
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }
	
	private ModifierType ReflectiveCarapace() {
		return new DefaultModifierType("Reflective Carapace") {
 
			private static final long serialVersionUID = 1L;
			@Override
			public ValidTargetResults modifierTypeOnTargetIsValidAction(Player attacker, Action action, Player target, GameState state) {
				if (Converter.isTrimmedSameIgnoreCase(action.getActionName(), "Magic Missile")) {
					return new ValidTargetResults(modifierName);
				}
				if (action.getSpell() != null) {
					Action exec = action.getSpell().createExecutionAction(action, attacker);
					if (exec != null && exec.getAttacks().size() > 0 && exec.getFirstAttack().isRollToHit()) {
						return new ValidTargetResults(modifierName);
					}
				}
				return null;
			}
		}; 
	}
  

}

