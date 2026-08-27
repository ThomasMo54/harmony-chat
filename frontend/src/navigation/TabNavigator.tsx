import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { useTranslation } from "react-i18next";
import { COLORS } from "@/constants/colors.ts";
import ContactListScreen from "@/screens/Tab/Contact/ContactList/ContactListScreen.tsx";

const Tab = createBottomTabNavigator();

function TabNavigator() {
  const { t } = useTranslation();

  return (
    <Tab.Navigator
      screenOptions={{
        headerShown: false,
        tabBarActiveTintColor: COLORS.primary,
      }}
    >
      <Tab.Screen name="ContactList" component={ContactListScreen} options={{ title: t('tabs.contactList') }} />
    </Tab.Navigator>
  );
}

export default TabNavigator;