/***************************************************************************
 *   Copyright (C) 2010-2015 by                                            *
 *      Itamar Syn-Hershko <itamar at code972 dot com>                     *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Affero General Public License           *
 *   version 3, as published by the Free Software Foundation.              *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU Affero General Public License for more details.                   *
 *                                                                         *
 *   You should have received a copy of the GNU Affero General Public      *
 *   License along with this program; if not, see                          *
 *   <http://www.gnu.org/licenses/>.                                       *
 **************************************************************************/
package org.apache.lucene.analysis.hebrew;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

public class TestHebrewQueryAnalyzer extends BaseTokenStreamTestCase {
    public void testBasics() throws IOException {
        Analyzer a = TestBase.getHebrewQueryAnalyzer();

        assertAnalyzesTo(a, "אימא", new String[]{"אימא$", "אימא"}); // recognized word, lemmatized
        assertAnalyzesTo(a, "אימא$", new String[]{"אימא$"}); // recognized word, lemmatized
        assertAnalyzesTo(a, "בדיקהבדיקה", new String[]{"בדיקהבדיקה$", "בדיקהבדיקה"}); // OOV
        assertAnalyzesTo(a, "בדיקהבדיקה$", new String[]{"בדיקהבדיקה$"}); // OOV
        assertAnalyzesTo(a, "ץץץץץץץץץץץ", new String[]{}); // Invalid, treated as noise
        assertAnalyzesTo(a, "ץץץץץץץץץץץ$", new String[]{}); // Invalid, treated as noise

        assertAnalyzesTo(a, "אנציקלופדיה", new String[]{"אנציקלופדיה$", "אנציקלופדיה"});
        assertAnalyzesTo(a, "אנצקלופדיה", new String[]{"אנצקלופדיה$", "אנציקלופדיה"});

        assertAnalyzesTo(a, "שמלות", new String[]{"שמלות$", "שמלה", "מל"});

        // Test non-Hebrew
        assertAnalyzesTo(a, "book", new String[]{"book$", "book"});
        assertAnalyzesTo(a, "book$", new String[]{"book$", "book"});
        assertAnalyzesTo(a, "steven's", new String[]{"steven's$", "steven's"});
        assertAnalyzesTo(a, "steven\u2019s", new String[]{"steven's$", "steven's"});
        //assertAnalyzesTo(a, "steven\uFF07s", new String[]{"steven's$", "steven's"});
        checkOneTerm(a, "3", "3");
    }

    public void testRegress() throws IOException {
        Analyzer analyzer = TestBase.getHebrewQueryAnalyzer();
        String input = TestBase.readFileToString("./../test-files/1371379368027561.txt");
        String[] output = {"שני$", "שניים", "שני", "ישן", "שינה", "שנה", "שני", "עשורים$", "עשור", "מההקמה$", "הקמה", "פעילות$", "פעילה", "פעילות",
                "מי$", "מי", "מים", "עדן$", "עדן", "עד", "עידן", "נמכרת$", "נמכר", "לפי$", "לפלנד", "פה", "פי", "שווי$", "שווי", "שווה", "שיווה",
                "וו", "של$", "של", "נשל", "240", "מיליון$", "מיליון", "יורו$", "הורה", "שוק$", "שוק", "ההון$", "הון", "מי$", "מי", "מים", "עדן$",
                "עדן", "עד", "עידן", "מעיינות$", "מעיין", "עיין", "עין", "החברה$", "חברה", "חבר", "הבורסאית$", "בורסאי", "המחזיקה$", "מחזיקה",
                "מחזיק", "החזיק", "70", "מהפעילות$", "פעילה", "פעילות", "בישראל$", "ישראל", "ישראל", "ובאירופה$", "אירופה", "תקבל$", "קיבל", "120",
                "מיליון$", "מיליון", "שקל$", "שקל", "קל", "נטו$", "נטו", "נטה", "הרוכשת$", "רוכשת", "רכש", "קרן$", "קרן", "ההשקעות$", "השקעה",
                "הפרטית$", "פרטי", "רואן$", "רואן", "קפיטל$", "קפיטל", "15", "כשני$", "שניים", "שני", "שני", "עשורים$", "עשור", "לאחר$", "אחרי",
                "איחר", "אחר", "שהוקמה$", "הוקם", "close$", "close", "מי$", "מי", "מים", "עדן$", "עדן", "עד", "עידן", "נתוני$", "נתון", "המסחר$",
                "מסחר", "סחר", "המלצות$", "המלצה", "אנליסטים$", "אנליסט", "הרשמה$", "הרשמה", "רשם", "לנתונים$", "נתון", "בזמן$", "זמן", "זימן",
                "אמת$", "אמת", "אמה", "אימת", "חדשות$", "חדשה", "חדש", "חברה$", "חברה", "חבר", "חיבר", "הוסף$", "הוסיף", "הוסף", "לתיק$", "תיק",
                "אישי$", "איש", "אישי", "לגרף$", "גרף", "גירף", "ניתוח$", "ניתוח", "טכני$", "טכני", "מי$", "מי", "מים", "עדן$", "עדן", "עד",
                "עידן", "מעיינות$", "מעיין", "עיין", "עין", "דיווחה$", "דיווח", "הבוקר$", "בוקר", "כי$", "כי", "מכרה$", "מכרה", "מכר", "כר",
                "רה", "את$", "את", "את", "פעילותה$", "פעילות", "בישראל$", "ישראל", "ישראל", "ובאירופה$", "אירופה", "לידי$", "יד", "קרן$", "קרן",
                "רון$", "רון", "רנן", "קפיטל$", "קפיטל", "rhone$", "rhone", "capital$", "capital", "לפי$", "לפלנד", "פה", "פי", "שווי$", "שווי",
                "שווה", "שיווה", "וו", "של$", "של", "נשל", "240", "מיליון$", "מיליון", "יורו$", "הורה", "1", "2", "מיליארד$", "מיליארד", "שקל$",
                "שקל", "קל", "מאחר$", "איחר", "אחרי", "אחר", "שהפעילות$", "פעילה", "פעילות", "מוחזקת$", "הוחזק", "בידי$", "יד", "החברה$", "חברה",
                "חבר", "הבת$", "בת", "70", "עדן$", "עדן", "עד", "עידן", "הולנד$", "הולנד", "חלקה$", "חלקה", "חלק", "חילק", "של$", "של", "נשל",
                "מי$", "מי", "מים", "עדן$", "עדן", "עד", "עידן", "מעיינות$", "מעיין", "עיין", "עין", "בעסקה$", "עסקה", "עסק", "עומד$", "עומד",
                "עמד", "על$", "על", "70", "מיליון$", "מיליון", "יורו$", "הורה", "אולם$", "אולם", "התמורה$", "תמורה", "נטו$", "נטו", "נטה",
                "בסופו$", "סוף", "של$", "של", "נשל", "דבר$", "דבר", "דיבר", "צפויה$", "צפוי", "להסתכם$", "הסתכם", "24", "24", "מיליון$", "מיליון",
                "יורו$", "הורה", "120", "מיליון$", "מיליון", "שקל$", "שקל", "קל", "בניכוי$", "ניכוי", "הלוואות$", "הלוואה", "בעלים$", "בעל",
                "עלה", "והוצאות$", "הוצאה", "עסקה$", "עסקה", "עסק", "שונות$", "שונות", "שנה", "שונה", "מחיר$", "מחיר", "המכירה$", "מכירה", "הכיר",
                "כירה", "הסופי$", "סוף", "סופי", "נמצא$", "נמצא", "מצא", "ברף$", "רף", "התחתון$", "תחתון", "של$", "של", "נשל", "טווח$", "טווח",
                "טיווח", "השווי$", "השווה", "שווי", "שווה", "לעסקה$", "עסקה", "עסק", "כפי$", "כף", "כפה", "פה", "פי", "שהוערך$", "הוערך", "בשוק$",
                "שוק", "במהלך$", "מהלך", "מהל", "הילך", "החודשים$", "חודש", "האחרונים$", "אחרון", "עם$", "עם", "פרסום$", "פרסום",
                "הדו\"חות$", "דו\"ח", "הכספיים$", "כספי", "לרבעון$", "רבעון", "הראשון$", "ראשון", "של$", "של", "נשל", "2013", "מי$", "מי", "מים",
                "עדן$", "עדן", "עד", "עידן", "מעיינות$", "מעיין", "עיין", "עין", "דיווחה$", "דיווח", "כי$", "כי", "העסקה$", "העסקה", "עסקה", "עסק",
                "צפויה$", "צפוי", "להיסגר$", "נסגר", "במחיר$", "מחיר", "הנמוך$", "נמוך", "170", "מיליון$", "מיליון", "שקל$", "שקל", "קל",
                "כתוצאה$", "תוצאה", "תוצא", "מכך$", "כך", "מניית$", "מניה", "מנייה", "החברה$", "חברה", "חבר", "צנחה$", "צנח", "במהלך$", "מהלך",
                "מהל", "הילך", "המסחר$", "מסחר", "סחר", "34", "כך$", "כך", "ששווייה$", "שווי", "של$", "של", "נשל", "מי$", "מי", "מים", "עדן$",
                "עדן", "עד", "עידן", "מעיינות$", "מעיין", "עיין", "עין", "עומד$", "עומד", "עמד", "כעת$", "עת", "של$", "של", "נשל", "113",
                "מיליון$", "מיליון", "שקל$", "שקל", "קל", "מעט$", "מעט", "מיעט", "מעט", "עט", "מתחת$", "מתח", "תחת", "לשווי$", "שווי", "שווה",
                "העסקה$", "העסקה", "עסקה", "עסק", "הסופי$", "סוף", "סופי", "מי$", "מי", "מים", "עדן$", "עדן", "עד", "עידן", "צילום$", "צילום",
                "עופר$", "עופר", "וקנין$", "קניין", "קרן$", "קרן", "רואן$", "רואן", "קפיטל$", "קפיטל", "הינה$", "יין", "קרן$", "קרן", "פרייוט$",
                "פרט", "פרוט", "פירוט", "אקוויטי$", "אקוטי", "שנוסדה$", "נוסד", "בשנת$", "שנת", "שינה", "שנה", "1996", "הקרן$", "הקרין", "קרן",
                "הינה$", "יין", "גלובלית$", "גלובלי", "ומתמקדת$", "התמקד", "בהשקעות$", "השקעה", "בפעילויות$", "פעילות", "מובילות$", "הוביל",
                "עם$", "עם", "נוכחות$", "נוכחת", "נוכחות", "נוכח", "נכח", "פן$", "פן", "פן", "אירופאית$", "אירופה", "או$", "או", "טרנס$", "טרנס",
                "אטלנטית$", "אטלנטי", "בעלות$", "בעלות", "בעל", "עלות", "עלה", "פוטנציאל$", "פוטנציאל", "צמיחה$", "צמיחה", "רב$", "רב", "לאומי$",
                "לאום", "לאומי", "אום", "לרואן$", "לרואן", "משרדים$", "משרד", "בלונדון$", "לונדון", "ובניו$", "בן", "יורק$", "יורק", "הורק", "ירק",
                "ונכון$", "נכון", "להיום$", "היום", "היא$", "היא", "מחזיקה$", "מחזיקה", "מחזיק", "החזיק", "בפורטפוליו$", "בפורטפוליו", "השקעות$",
                "השקעה", "מגוון$", "מגוון", "גוון", "גיוון", "גו", "כולל$", "כלל", "השקעות$", "השקעה", "בכימיקלים$", "כימיקל", "מוצרי$", "מוצר",
                "הוצר", "צריכה$", "צריכה", "צריך", "מזון$", "מזון", "תעשיה$", "תעשייה", "כריה$", "כר", "ותובלה$", "תובלה", "תובל", "במהלך$",
                "מהלך", "מהל", "הילך", "התקופה$", "תקופה", "תקוף", "האחרונה$", "אחרונה", "אחרון", "פועלת$", "פועלת", "פעל", "מי$", "מי", "מים",
                "עדן$", "עדן", "עד", "עידן", "הולנד$", "הולנד", "למימוש$", "מימוש", "אסטרטגיה$", "אסטרטגיה", "אסטרטג", "עסקית$", "עסקי", "חדשה$",
                "חדשה", "חידש", "חדש", "הכוללת$", "כלל", "הגברת$", "הגברה", "הגביר", "גברת", "גבר", "הפעילות$", "פעילה", "פעילות", "בשוק$", "שוק",
                "הקפה$", "הקפה", "קפה", "קפה", "על$", "על", "חשבון$", "חשבון", "הפעילות$", "פעילה", "פעילות", "המסורתית$", "מסורתי", "בתחום$",
                "תחום", "תחם", "כדי$", "כד", "די", "די", "ובקבוקי$", "בקבוק", "קבוקי", "המים$", "מים", "ים", "הקרן$", "הקרין", "קרן", "החדשה$",
                "חדשה", "חדש", "עתידה$", "עתיד", "להמשיך$", "המשיך", "ולהשקיע$", "השקיע", "בפעילות$", "פעילה", "פעילות", "הקפה$", "הקפה", "קפה",
                "קפה", "ולהרחיבה$", "הרחיב", "באירופה$", "אירופה", "מנכ\"ל$", "מנכ\"ל", "מי$", "מי", "מים", "עדן$", "עדן", "עד", "עידן", "הולנד$",
                "הולנד", "רענן$", "רענן", "זילברמן$", "זילברמן", "שימשיך$", "המשיך", "לנהל$", "ניהל", "את$", "את", "את", "החברה$", "חברה", "חבר",
                "גם$", "גם", "תחת$", "תחת", "הקרן$", "הקרין", "קרן", "החדשה$", "חדשה", "חדש", "הדגיש$", "הדגיש", "שהרכישה$", "רכישה", "מהווה$",
                "היווה", "הווה", "הזדמנות$", "הזדמנות", "להאיץ$", "האיץ", "את$", "את", "את", "ההתרחבות$", "התרחבות", "האסטרטגיה$", "אסטרטגיה",
                "אסטרטג", "העסקית$", "עסקי", "כפי$", "כף", "כפה", "פה", "פי", "שהותוותה$", "הותווה", "בשנים$", "שנים", "שנה", "האחרונות$",
                "אחרונה", "הכוללת$", "כלל", "פיתוח$", "פיתוח", "נוסף$", "נוסף", "הוסף", "של$", "של", "נשל", "סל$", "סל", "מלא$", "מילא", "מלא",
                "לא", "של$", "של", "נשל", "מוצרי$", "מוצר", "הוצר", "מים$", "מים", "ים", "ושירותים$", "שירות", "נלווים$", "נלווה", "במקביל$",
                "מקביל", "הקביל", "לפיתוח$", "פיתוח", "שירותי$", "שירות", "קפה$", "קפה", "קפה", "למשרדים$", "משרד", "ובתים$", "בית", "דורשת$",
                "דרש", "השקעות$", "השקעה", "משמעותיות$", "משמעותי", "בשיווק$", "שיווק", "ציוד$", "ציוד", "פיתוח$", "פיתוח", "עסקים$", "עסק",
                "וברכישת$", "רכישה", "חברות$", "חברה", "חברות", "משלימות$", "השלים", "ימה", "אנחנו$", "אנחנו", "בוחנים$", "בחן", "גם$", "גם",
                "את$", "את", "את", "האפשרות$", "אפשרות", "להיכנס$", "נכנס", "לשווקים$", "שוק", "בינלאומיים$", "בינלאומי", "נוספים$", "נוסף",
                "ובמיוחד$", "יוחד", "מיוחד", "למזרח$", "מזרח", "אירופה$", "אירופה", "בה$", "בי", "הדרישה$", "דרישה", "למים$", "מים", "מינראליים$",
                "מינראליים", "גוברת$", "גבר", "אנו$", "אנו", "שמחים$", "שמח", "לשתף$", "שיתף", "פעולה$", "פעולה", "עם$", "עם", "קרן$", "קרן",
                "רואן$", "רואן", "בישום$", "בישום", "החזון$", "חזון", "המשותף$", "שותף", "ומאמינים$", "מאמין", "האמין", "אמין", "כי$", "כי",
                "לניסיונה$", "ניסיון", "של$", "של", "נשל", "רואן$", "רואן", "ביצירת$", "יצירה", "ערך$", "ערך", "לפעילויות$", "פעילות", "נרכשות$",
                "נרכש", "תהייה$", "תהייה", "תרומה$", "תרומה", "רומה", "תרום", "משמעותית$", "משמעותי", "להמשך$", "המשך", "התפתחותה$", "התפתחות",
                "של$", "של", "נשל", "החברה$", "חברה", "חבר", "בעלי$", "בעל", "על", "עלה", "המניות$", "מניה", "מנייה", "היוצאים$", "יצא", "יוצא",
                "רוני$", "רון", "רנן", "ויהודה$", "יהודה", "נפתלי$", "נפתלי", "נפתל", "70", "וקרן$", "קרן", "אוך$", "אוך", "זיף$", "זיף", "27",
                "25", "החליטו$", "החליט", "לממש$", "ממש", "מימש", "את$", "את", "את", "החברה$", "חברה", "חבר", "כחלק$", "חלק", "חילק", "מהסכם$",
                "הסכם", "ההשקעה$", "השקעה", "שנחתם$", "נחת", "נחתם", "נח", "בין$", "בין", "ין", "האחים$", "אח", "נפתלי$", "נפתלי", "נפתל",
                "לקרן$", "קרן", "2007", "הקרן$", "הקרין", "קרן", "השקיעה$", "השקיע", "שקיעה", "אז$", "אז", "כמה$", "כמה", "כמה", "עשרות$", "עשרת",
                "מיליוני$", "מיליון", "יורו$", "הורה", "בחברה$", "חברה", "חבר", "חיבר", "והעניקה$", "העניק", "לה$", "לי", "לה", "הלוואת$",
                "הלוואה", "בעלים$", "בעל", "עלה", "של$", "של", "נשל", "25", "מיליון$", "מיליון", "יורו$", "הורה", "כחלק$", "חלק", "חילק",
                "מההסכם$", "הסכם", "ניתנה$", "ניתן", "לקרן$", "קרן", "אופציה$", "אופציה", "שנכנסה$", "נכנס", "לתקוף$", "תקף", "תקוף", "במהלך$",
                "מהלך", "מהל", "הילך", "דצמבר$", "דצמבר", "2012", "לממש$", "ממש", "מימש", "השקעתה$", "השקעה", "ולאלץ$", "אילץ", "גם$", "גם",
                "את$", "את", "את", "שאר$", "שאר", "אר", "בעלי$", "בעל", "על", "עלה", "המניות$", "מניה", "מנייה", "למכור$", "מכר", "מכור", "את$",
                "את", "את", "מניותיהם$", "מניה", "מנייה",};
        assertAnalyzesTo(analyzer,input,output);
    }
}
