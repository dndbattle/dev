/**
 * 
 */
package com.dndcombat.monsters.b;

import java.util.List;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.conditions.Restrained;
import com.dndcombat.fight.model.AttackResult;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.Damage;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.custom.SpellAction;
import com.dndcombat.monster.custom.SpellWeapon;
import com.dndcombat.util.Converter;

/**
 
 */
public class Behir extends Player {
 
	private static final long serialVersionUID = 1L;

	public Behir() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Behir");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Behir.webp");

	    str(23).dex(16).con(18).inte(7).wis(14).cha(12);

		// Saving Throws
		strSave(6).dexSave(3).conSave(4).intSave(-2).wisSave(2).chaSave(1);
	
		 // Combat Stats
		ac(17).init(3).hp(168).speed(50).cr(11);

        // Size and Type
	    size(RefList.creaturesizeshuge).type(RefList.creaturetypesmonstrosity);
  
        // Multiattack
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Bite(), Constrict()));
        
        // Actions
        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));
        addAction(new Action("Constrict", RefList.actiontypesmainaction)
                .addAttack(Constrict()));
        
        getCreature().addModifierType(ConstrictMod());
    }
    

	private Attack Bite() {
		
        Attack a = new Attack("Bite");
        a.setAttackBonus(10)
         .setMeleereach(10)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(2)
         .dieType(12)
         .bonus(6)
         .damageTypeRefId(RefList.damagetypespiercing);
        a.addDamage().nbrDice(2).dieType(10).damageTypeRefId(RefList.damagetypeslightning);
        
        return a;
    }
	

	private Attack Constrict() {
       Attack a = new Attack("Constrict");
       SpellWeapon sw = SpellWeapon.builder().breathName(a.getAttackName())
			.actionTypeRefId(RefList.actiontypesmainaction)
			.castRangeFeet(5)
			.saveDC(18)
			.halfDamageOnSave(false)
			.saveVsRefId(RefList.savingthrowvsstrength)
			.conditionRefId(RefList.conditionsgrappled)
			.damage(new DamageModel().nbrDice(5).dieType(8).bonus(6).damageTypeRefId(RefList.damagetypesbludgeoning))
			.build();
				
        a.setSpell(new SpellAction(sw));
        return a;
    }
	
	private ModifierType ConstrictMod() {
		return new DefaultModifierType("Constrict") {
		 
			private static final long serialVersionUID = 1L;

			@Override
			public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target, List<Damage> list, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
				if (!Converter.isTrimmedSameIgnoreCase(attack.getAttackName(), "Constrict")) {
					return false;
				}
				if (source.isRestrainingSomeone(state)) {
					// already grappled
				} else if (target.isActive()) {
		       		Restrained r = new Restrained(source, action, new DurationType(10), state);
		       		target.addModifier(r);
		       		state.addActionLog(source.getName() + " uses " + modifierName + " to restrain " + target.getName());
		      
		       	}
				return false;
			}
			
			 
		};
	}	
	 
}
