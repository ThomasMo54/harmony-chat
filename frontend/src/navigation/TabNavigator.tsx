import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { useTranslation } from "react-i18next";
import { COLORS } from "../constants/colors.ts";
import LoginScreen from "../screens/Auth/Login/LoginScreen.tsx";

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
      <Tab.Screen name="Home" component={LoginScreen} options={{ title: t('tabs.home') }} />
    </Tab.Navigator>
  );
}

export default TabNavigator;