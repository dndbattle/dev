/**
 * 
 */
package com.dndcombat.monsters.g;

import java.util.ArrayList;
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
import com.dndcombat.monster.traits.Parry;

/**
 
 */
public class Gladiator extends Player {
 
	private static final long serialVersionUID = 1L;

	public Gladiator() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Gladiator");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Gladiator.webp");

	    str(18).dex(15).con(16).inte(10).wis(12).cha(15);

		// Saving Throws
		strSave(+7).dexSave(+5).conSave(+6).intSave(+0).wisSave(+4).chaSave(+2);
	
		 // Combat Stats
		ac(16).init(+5).hp(112).speed(30).cr(5);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);

	/*
	- Multiattack for melee and ranged
	- addDamage on each attack
	
	- saving throws ending on the targets next turn have a duration of 0
	- saving throws triggered on hit can be added to the action (duration customized)
	*/
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Spear(), ShieldBash(), Spear()));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(SpearThrown(), SpearThrown(), SpearThrown()));


        addAction(new Action("Spear", RefList.actiontypesmainaction)
                .addAttack(Spear()));
 
        addAction(new Action("Shield Bash", RefList.actiontypesmainaction)
                .addAttack(ShieldBash()));
 
       addAction(new Action("Spear Thrown", RefList.actiontypesmainaction)
                .addAttack(SpearThrown()));
       
       getCreature().addModifierType(new Parry(3));

	}

    private Attack ShieldBash() {
        Attack a = new Attack("Shield Bash");
        List<DamageModel> damage = new ArrayList<DamageModel>();
        damage.add(new DamageModel().nbrDice(2).dieType(4).bonus(4).damageTypeRefId(RefList.damagetypesbludgeoning).autoApplyCondition(RefList.conditionsprone, new DurationType(10)));
        SaveDC save = new SaveDC();
        save.saveVsRefId = RefList.savingthrowvsstrength;
        save.dc = 15;
        save.conditionRefId = RefList.conditionsprone;
       
        WithinXFeetEffect w = new WithinXFeetEffect(5, a.getAttackName(), damage, save, 0);
    
        a.setSpell(w);
        return a;
    }

    private Attack Spear() {
        Attack a = new Attack("Spear"); 
        a.setAttackBonus(7) 
         .setMeleereach(5) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        return a;
    }
    
    private Attack SpearThrown() {
        Attack a = new Attack("Spear Thrown"); 
        a.setAttackBonus(7) 
         .setAttackRange(20) // 0 if not found
         .setLongRange(60) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        return a;
    }

}

