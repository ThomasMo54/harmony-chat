import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { useAuth } from "@/context/AuthContext.tsx";
import { ActivityIndicator, StyleSheet, View } from "react-native";
import AuthNavigator from "./AuthNavigator.tsx";
import TabNavigator from "./TabNavigator.tsx";

const Stack = createNativeStackNavigator();

function RootNavigator() {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <View style={styles.loadingView}>
        <ActivityIndicator size="large" />
      </View>
    );
  }

  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      {isAuthenticated ? (
        <Stack.Screen name="App" component={TabNavigator} />
      ) : (
        <Stack.Screen name="Auth" component={AuthNavigator} />
      )}
    </Stack.Navigator>
  );
}

const styles = StyleSheet.create({
  loadingView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
});

export default RootNavigator;