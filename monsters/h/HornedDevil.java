/**
 * 
 */
package com.dndcombat.monsters.h;

import java.util.Arrays;
import java.util.List;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.AttackResult;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Modifier;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.Damage;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.WithinXFeetEffect;
import com.dndcombat.monster.traits.MagicResistance;
import com.dndcombat.util.Roll;
import com.dndcombat.util.RollResult;

/**
 
 */
public class HornedDevil extends Player {
 
	private static final long serialVersionUID = 1L;

	public HornedDevil() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Horned Devil");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Horned Devil.webp");

	    str(22).dex(17).con(21).inte(12).wis(16).cha(18);

		// Saving Throws
		strSave(+10).dexSave(+7).conSave(+5).intSave(+1).wisSave(+7).chaSave(+8);
	
		 // Combat Stats
		ac(18).init(+7).hp(199).speed(30).flySpeed(60).cr(11);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesfiend);

	    damageResist(RefList.damagetypescold);
		damageImmune(RefList.damagetypespoison);
		damageImmune(RefList.damagetypesfire);
		conditionImmune(RefList.conditionspoisoned);
		getCreature().addModifierType(new MagicResistance());


        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(SearingFork(), SearingFork(), InfernalTail() ));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(HurlFlame(), HurlFlame(), HurlFlame() ));

        addAction(new Action("Searing Fork", RefList.actiontypesmainaction)
                .addAttack(SearingFork()));
        
       addAction(new Action("Hurl Flame", RefList.actiontypesmainaction)
                .addAttack(HurlFlame()));

	}


	private Attack SearingFork() {
	
        Attack a = new Attack("Searing Fork"); 
        a.setAttackBonus(10) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(6) 
         .damageTypeRefId(RefList.damagetypespiercing);
        a.addDamage().nbrDice(2).dieType(8).damageTypeRefId(RefList.damagetypesfire);
        
        return a;
    }
    private Attack HurlFlame() {
        Attack a = new Attack("Hurl Flame"); 
        a.setAttackBonus(8)
         .setAttackRange(150) 
         .setLongRange(0)
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(5)
         .dieType(8) 
         .bonus(4)
         .damageTypeRefId(RefList.damagetypesfire);
        
         
        // a.setThrownRange(150, 0); 
        return a;
    }
    

	private Attack InfernalTail() {
        Attack a = new Attack("Infernal Tail"); 
		
		SaveDC save = new SaveDC();
		save.dc = 17;
		save.saveVsRefId = RefList.savingthrowvsdexterity;
		WithinXFeetEffect w = new WithinXFeetEffect(10, a.getAttackName(), Arrays.asList(new DamageModel().nbrDice(1).dieType(8).bonus(6).damageTypeRefId(RefList.damagetypesnecrotic)),
				save, 1);
		w.maxNbrTargets = 1;
		a.setSpell(w);
		this.getCreature().getModifierTypes().add(new DefaultModifierType(a.getAttackName()) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target, List<Damage> list, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
				int necro = 0;
				for (Damage d : damageSoFar) {
					if (d.damageTypeRefId== RefList.damagetypesnecrotic) {
						necro += d.dmg;
					}
				}
				if (necro > 0) {
					Modifier m = new Modifier(modifierName, new DefaultModifierType(modifierName) {
						//TODO heal fixes or medicine check
						private static final long serialVersionUID = 1L;
						
						public void statusModifierOnTargetReceivingHealing(Player healer, java.util.List<Damage> heals, Action action, Player target, GameState state) {
							target.removeModifier(modifierName);
						};
						
						public boolean applyModifierOnMeStartOfMyTurnRemoveIfTrue(Player me, Modifier m, GameState state) {
							if (target.getSpellSlots().getTurnCastTimes(modifierName) > 0) {
								return false;
							}
							RollResult rr = Roll.nd(3, 6);
							String killed = "";
							target.getStatus().damage(rr.getTotal(), source, false);
							if (target.getStatus().getCurrentHitPoints() <= 0) {
								killed = ", killed";
							}
							state.addActionLog(target.getName() + " loses " + rr.getTotal() + " hp from infernal wound" + killed);
							target.markCast(modifierName);
							return false;
						}
						
					}, source, action, new DurationType(10), state);
					target.getStatus().addModifiers(m);
				}
				return false;
			}
			
		});
		return a;
	}

}

