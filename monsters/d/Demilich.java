/**
 * 
 */
package com.dndcombat.monsters.d;

import java.util.Arrays;

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
public class Demilich extends Player {
 
	private static final long serialVersionUID = 1L;

	public Demilich() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Demilich");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Demilich.webp");

	    str(1).dex(20).con(10).inte(20).wis(17).cha(20);

		// Saving Throws
		strSave(-5).dexSave(+11).conSave(+6).intSave(+11).wisSave(+9).chaSave(+5);
	
		 // Combat Stats
		ac(20).init(+17).hp(180).speed(5).flySpeed(30).cr(18);

        // Size and Type
	    size(RefList.creaturesizestiny).type(RefList.creaturetypesundead);

		getCreature().setLegendaryResistances(3);
		getCreature().setLegendaryActions(3);

		damageResist(RefList.damagetypesslashing);
		damageResist(RefList.damagetypespiercing);
		damageResist(RefList.damagetypesbludgeoning);
		
		damageImmune(RefList.damagetypesnecrotic);
		damageImmune(RefList.damagetypespoison);
		damageImmune(RefList.damagetypespsychic);
		
		conditionImmune(RefList.conditionscharmed);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionsfrightened);
		conditionImmune(RefList.conditionsparalyzed);
		conditionImmune(RefList.conditionspetrified);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionsprone);
		conditionImmune(RefList.conditionsstunned);
			
	/*
	- Multiattack for melee and ranged
	- addDamage on each attack
	
	- saving throws ending on the targets next turn have a duration of 0
	- saving throws triggered on hit can be added to the action (duration customized)
	- save or damage/effect is WithinXFeetEffect w = new WithinXFeetEffect(5, a.getAttackName(), damage, save, 0); see gladiator. 1/2 dmg on save is not the default
	*/
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(NecroticBurst(), NecroticBurst(), NecroticBurst() ));
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(NecroticBurstRanged(), NecroticBurstRanged(), NecroticBurstRanged() ));
        
        addAction(new Action("Howl", RefList.actiontypesmainaction)
                .addAttack(Howl() ));

        //TODO Grave-Dust Flight
        addAction(EnergyDrain());
        addAction(Necrosis());

	}

    private Attack NecroticBurst() {
        Attack a = new Attack("Necrotic Burst"); 
        a.setAttackBonus(11) 
         .setMeleereach(5) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(7) 
         .dieType(6) 
         .damageTypeRefId(RefList.damagetypesnecrotic); 
        return a;
    }
    
    private Attack NecroticBurstRanged() {
        Attack a = new Attack("Necrotic Burst Ranged"); 
        a.setAttackBonus(11) 
         .setAttackRange(120) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(7) 
         .dieType(6) 
         .damageTypeRefId(RefList.damagetypesnecrotic); 
        return a;
    } 
    
    //TODO if you miss everyone with this probably dont want to use again
    private Attack Howl() {
        Attack a = new Attack("Howl"); 
        SaveDC save = new SaveDC();
        save.dc = 19;
        save.saveVsRefId = RefList.savingthrowvsconstitution;
        save.conditionRefId = RefList.conditionsfrightened;
        save.duration = new DurationType(1);
        
        WithinXFeetEffect w = new WithinXFeetEffect(30, a.getAttackName(),
        		Arrays.asList(new DamageModel().nbrDice(20).dieType(6).damageTypeRefId(RefList.damagetypespsychic)), 
        		save, 0);
        w.recharge(5);
        a.setSpell(w);
        return a;
    }  
    

    private Action EnergyDrain() {
    	Action action = new Action("Energy Drain", RefList.actiontypeslegendaryaction);
        Attack a = new Attack("Energy Drain"); 
        SaveDC save = new SaveDC();
        save.dc = 19;
        save.saveVsRefId = RefList.savingthrowvsconstitution;
        
        WithinXFeetEffect w = new WithinXFeetEffect(30, a.getAttackName(),
        		Arrays.asList(new DamageModel().nbrDice(14).dieType(6).damageTypeRefId(RefList.damagetypespsychic)), 
        		save, 1);
        w.recharge(5);
        w.attackSettings.reduceHPMax = true;
        
        action.setSpell(w, new SpellSlot(0));
        action.setMaxUsesPerRound(1);
        action.addAttack(a);
        return action;
    }

    private Action Necrosis() {
    	Action action = new Action("Necrotic Burst",RefList.actiontypeslegendaryaction);
        Attack a = new Attack(action.getActionName()); 
        a.setAttackBonus(11) 
         .setAttackRange(120) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(7) 
         .dieType(6) 
         .damageTypeRefId(RefList.damagetypesnecrotic);
        action.setLegendaryCost(1);
        
        return action;
    } 
    
}

