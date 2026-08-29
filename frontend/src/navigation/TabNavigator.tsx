import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { useTranslation } from 'react-i18next';
import { COLORS } from '@/constants/colors.ts';
import AppIcon from '@/components/AppIcon/AppIcon.tsx';
import ContactNavigator from '@/navigation/ContactNavigator.tsx';

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
      <Tab.Screen
        name="ContactList"
        component={ContactNavigator}
        options={{
          title: t('tabs.contactList'),
          tabBarIcon: ({ color, size }) => (
            <AppIcon name="list" size={size} color={color} />
          ),
        }}
      />
    </Tab.Navigator>
  );
}

export default TabNavigator;
