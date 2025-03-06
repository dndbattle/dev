/**
 * 
 */
package com.dndcombat.monsters.b;

import java.util.Arrays;
import java.util.List;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.WithinXFeetEffect;
import com.dndcombat.spells.basic.SpellSlot;

/**
 
 */
public class BanditDeceiver extends Player {
 
	private static final long serialVersionUID = 1L;

	public BanditDeceiver() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Bandit Deceiver");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Bandit.webp");

	    str(8).dex(16).con(14).inte(17).wis(12).cha(16);

		// Saving Throws
		strSave(-1).dexSave(+6).conSave(+2).intSave(+6).wisSave(+1).chaSave(+3);
	
		 // Combat Stats
		ac(16).init(+6).hp(130).speed(30).cr(7);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);
 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Dagger(),Dagger(),Dagger() ));
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(DaggerThrown(),DaggerThrown(),DaggerThrown() ));


       addAction(new Action("Dagger", RefList.actiontypesmainaction)
                .addAttack(Dagger()));


       addAction(blindingFlash());

       addAction(new Action("Dagger Thrown", RefList.actiontypesmainaction)
                .addAttack(DaggerThrown()));

	}

    private Action blindingFlash() {
		Action a = new Action("Blinding Flash", RefList.actiontypesmainaction);
		List<DamageModel> damage = Arrays.asList(new DamageModel().nbrDice(3).dieType(6).bonus(3).damageTypeRefId(RefList.damagetypesradiant));
		damage.get(0).autoApplyCondition(RefList.conditionsblinded, new DurationType(1));
		SaveDC save = new SaveDC().dc(14).saveVsRefId(RefList.savingthrowvsconstitution).conditionRefId(RefList.conditionsblinded);
		WithinXFeetEffect w = new WithinXFeetEffect(10, a.getActionName(), damage, save, 0);
		w.attackSettings.halfDamage = true;
		w.recharge(4);
		a.setSpell(w, new SpellSlot(0));
 
		return a;
	}

	private Attack Dagger() {
        Attack a = new Attack("Dagger"); 
        a.setAttackBonus(6) 
         .setMeleereach(5) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(4) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        return a;
    }
    
    private Attack DaggerThrown() {
        Attack a = new Attack("Dagger Thrown"); 
        a.setAttackBonus(6) 
         .setAttackRange(20) // 0 if not found
         .setLongRange(60) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(4) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        return a;
    }

}

