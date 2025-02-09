/**
 * 
 */
package com.dndcombat.monsters.b;

import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.DurationType.WhenToEnd;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.FireAura;
import com.dndcombat.monster.traits.MagicResistance;

/**
 
 */
public class Balor extends Player {
 
	private static final long serialVersionUID = 1L;

	public Balor() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Balor");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Balor.webp");

        // Ability Scores
        str(26).dex(15).con(22).inte(20).wis(16).cha(22);

        // Saving Throws
        strSave(8).dexSave(2).conSave(12).intSave(5).wisSave(9).chaSave(6);

        // Combat Stats
        ac(19).init(14).hp(287).speed(40).flySpeed(80).cr(19);

        // Size and Type
	    size(RefList.creaturesizeshuge).type(RefList.creaturetypesfiend);
	    
	    // damage resistance
	    damageResist(RefList.damagetypescold, RefList.damagetypeslightning);
	    damageImmune(RefList.damagetypesfire, RefList.damagetypespoison);
	    conditionImmune(RefList.conditionscharmed, RefList.conditionsfrightened, RefList.conditionspoisoned);
	    
	    // TODO death throes
	    // fire aura
        getCreature().addModifierType(new FireAura());
        // legendary resistance
	    getCreature().setLegendaryResistances(3);
	    // magic resistance
	    getCreature().addModifierType(new MagicResistance());
	    
        // Actions
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(FlameWhip(),LightningBlade()));
    
        // Actions
        addAction(new Action("Flame Whip", RefList.actiontypesmainaction)
                .addAttack(FlameWhip()));
        addAction(new Action("Lightning Blade", RefList.actiontypesmainaction)
                .addAttack(LightningBlade()));
        
        // Bonus actions
        // TODO teleport
    }
    

	private Attack FlameWhip() {
		
        Attack a = new Attack("Flame Whip");
        a.setAttackBonus(14)
         .setMeleereach(30)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(3)
         .dieType(6)
         .bonus(8)
         .damageTypeRefId(RefList.damagetypesforce);
        a.addDamage()
        .nbrDice(5)
        .dieType(6)
        .dragNextToMe()
        .damageTypeRefId(RefList.damagetypesfire)
        .autoApplyCondition(RefList.conditionsprone, new DurationType(10))
        ;
        
        return a;
    }
	

	private Attack LightningBlade() {
		DurationType duration = new DurationType(1);
		duration.whenToEnd = WhenToEnd.StartOfTurn;
        Attack a = new Attack("Lightning Blade");
        a.setAttackBonus(14)
        .setMeleereach(10)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(3)
         .dieType(8)
         .bonus(8)
         .damageTypeRefId(RefList.damagetypesforce);
        a.addDamage()
        .nbrDice(4)
        .dieType(10)
        .autoApplyCondition(RefList.conditionsnoreactions, duration)
        .damageTypeRefId(RefList.damagetypeslightning);
        
        return a;
    }
	
	 
}
